package com.github.chriskn.structurizrextension.export.writer

import com.github.chriskn.structurizrextension.export.idOf
import com.github.chriskn.structurizrextension.model.icon
import com.github.chriskn.structurizrextension.model.link
import com.github.chriskn.structurizrextension.plantuml.DependencyConfiguration
import com.github.chriskn.structurizrextension.plantuml.Direction
import com.github.chriskn.structurizrextension.plantuml.IconRegistry
import com.github.chriskn.structurizrextension.plantuml.Mode
import com.github.chriskn.structurizrextension.view.LayoutRegistry
import com.github.chriskn.structurizrextension.view.renderAsSequenceDiagram
import com.structurizr.export.IndentingWriter
import com.structurizr.model.InteractionStyle
import com.structurizr.model.Relationship
import com.structurizr.view.DynamicView
import com.structurizr.view.ModelView
import com.structurizr.view.RelationshipView
import com.structurizr.view.View

private const val ASYNC_REL_TAG_NAME = "async relationship"
private const val C4_LAYOUT_DIRECTION = "c4:layout:direction"
private const val C4_LAYOUT_MODE = "c4:layout:mode"

class RelationshipWriter(
    private val propertyWriter: PropertyWriter
) {

    internal fun writeRelationships(view: ModelView, writer: IndentingWriter) {
        val dependencyConfigurations = LayoutRegistry.layoutForKey(view.key).dependencyConfigurations
        dependencyConfigurations.forEach { conf ->
            view.relationships
                .filter { conf.filter(it.relationship) }
                .map { relationshipView -> relationshipView.apply(conf, view) }
        }
        val sorted = if (view is DynamicView) {
            view.relationships.sortedBy { rv: RelationshipView ->
                rv.order
            }
        } else {
            view.relationships
                .sortedBy { rv: RelationshipView ->
                    rv.relationship.source.name + rv.relationship.destination.name
                }
        }
        sorted.forEach { rv: RelationshipView -> writeRelationship(view, rv, writer) }
    }

    internal fun writeRelationship(view: ModelView, relationshipView: RelationshipView, writer: IndentingWriter) {
        if (view is DynamicView && view.renderAsSequenceDiagram) {
            writeRelationshipSequenceDiagram(view, relationshipView, writer)
            return
        }
        val relationship = relationshipView.relationship
        var source = relationship.source
        var destination = relationship.destination
        if (relationshipView.isResponse == true) {
            source = relationship.destination
            destination = relationship.source
        }
        val relationshipBuilder = StringBuilder()
        val mode = determineMode(relationship, view)
        val relationshipType = determineType(mode, relationshipView)
        val description = determineDescription(view, relationshipView)

        if (view is DynamicView) {
            relationshipBuilder.append(
                """$relationshipType(${relationshipView.order},${idOf(source)}, ${idOf(destination)}, "$description""""
            )
        } else {
            relationshipBuilder.append(
                """$relationshipType(${idOf(source)}, ${idOf(destination)}, "$description""""
            )
        }
        if (relationship.technology != null) {
            relationshipBuilder.append(""", "${relationship.technology}"""")
        }
        val sprite = IconRegistry.iconFileNameFor(relationship.icon ?: "")
        if (!sprite.isNullOrBlank()) {
            relationshipBuilder.append(""", ${'$'}sprite=$sprite""")
        }
        if (!relationship.link.isNullOrBlank()) {
            relationshipBuilder.append(linkString(relationship.link))
        }
        if (relationship.interactionStyle == InteractionStyle.Asynchronous) {
            relationshipBuilder.append(""", ${'$'}tags="$ASYNC_REL_TAG_NAME"""")
        }
        relationshipBuilder.append(")")

        propertyWriter.writeProperties(relationshipView.relationship, writer)
        writer.writeLine(relationshipBuilder.toString())
    }

    private fun writeRelationshipSequenceDiagram(view: ModelView, relationshipView: RelationshipView, writer: IndentingWriter) {
        // Rel($from, $to, $label, $techn="", $descr="", $sprite="", $tags="", $link="", $index="", $rel=""
        val relationship = relationshipView.relationship
        var source = relationship.source
        var destination = relationship.destination
        if (relationshipView.isResponse == true) {
            source = relationship.destination
            destination = relationship.source
        }
        val description = "${relationshipView.order} ${determineDescription(view, relationshipView)}"

        val relationshipBuilder = StringBuilder()
        relationshipBuilder.append("""${Mode.Rel.macro}(${idOf(source)}, ${idOf(destination)}, "$description" """)

        if (relationship.technology != null) {
            relationshipBuilder.append(""", ${'$'}techn="${relationship.technology}"""")
        }

        val sprite = IconRegistry.iconFileNameFor(relationship.icon ?: "")
        if (!sprite.isNullOrBlank()) {
            relationshipBuilder.append(""", ${'$'}sprite="$sprite"""")
        }

        if (relationship.interactionStyle == InteractionStyle.Asynchronous) {
            relationshipBuilder.append(""", ${'$'}tags="$ASYNC_REL_TAG_NAME" """)
        }

        if (!relationship.link.isNullOrBlank()) {
            relationshipBuilder.append(linkString(relationship.link))
        }

        relationshipBuilder.append(")")

        propertyWriter.writeProperties(relationshipView.relationship, writer)
        writer.writeLine(relationshipBuilder.toString())
    }

    private fun determineMode(
        relationship: Relationship,
        view: ModelView
    ): Mode = when {
        relationship.properties.containsKey(C4_LAYOUT_MODE) -> Mode.valueOf(relationship.properties[C4_LAYOUT_MODE]!!)
        // sequence diagrams need no order
        view is DynamicView && !view.renderAsSequenceDiagram -> Mode.RelIndex
        else -> Mode.Rel
    }

    private fun determineDescription(
        view: ModelView,
        relationshipView: RelationshipView,
    ): String = if (view is DynamicView) {
        relationshipView.description
    } else {
        relationshipView.relationship.description
    }.ifEmpty { " " }

    private fun determineType(
        mode: Mode,
        relationshipView: RelationshipView,
    ): String = when (mode) {
        Mode.Rel, Mode.RelIndex -> {
            if (relationshipView.relationship.properties.containsKey(C4_LAYOUT_DIRECTION)) {
                val direction = determineDirection(relationshipView)
                "${mode.macro}_${direction.macro()}"
            } else {
                mode.macro
            }
        }
        else -> "Rel_${mode.macro}"
    }

    private fun determineDirection(
        relationshipView: RelationshipView,
    ): Direction {
        val direction = Direction.valueOf(relationshipView.relationship.properties[C4_LAYOUT_DIRECTION]!!)
        if (relationshipView.isResponse == true) {
            return direction.inverse()
        }
        return direction
    }

    private fun RelationshipView.apply(
        conf: DependencyConfiguration,
        view: View
    ): Relationship {
        val rel = this.relationship
        val direction = conf.direction
        val mode = conf.mode
        if (direction != null) {
            rel.addProperty(C4_LAYOUT_DIRECTION, direction.name)
        }
        if (mode != null) {
            require(view !is DynamicView) {
                "Setting the dependency mode is not supported fot dynamic views"
            }
            rel.addProperty(C4_LAYOUT_MODE, mode.name)
        }
        return rel
    }
}
