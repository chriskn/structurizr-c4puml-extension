package com.github.chriskn.structurizrextension.export.writer

import com.github.chriskn.structurizrextension.export.idOf
import com.github.chriskn.structurizrextension.model.icon
import com.github.chriskn.structurizrextension.model.link
import com.github.chriskn.structurizrextension.plantuml.DependencyConfiguration
import com.github.chriskn.structurizrextension.plantuml.Direction
import com.github.chriskn.structurizrextension.plantuml.IconRegistry
import com.github.chriskn.structurizrextension.plantuml.Mode
import com.github.chriskn.structurizrextension.view.LayoutRegistry
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
        propertyWriter.writeProperties(relationshipView.relationship, writer)

        val relationship = relationshipView.relationship
        var source = relationship.source
        var destination = relationship.destination
        if (relationshipView.isResponse == true) {
            source = relationship.destination
            destination = relationship.source
        }
        val mode = when {
            relationship.properties.containsKey(C4_LAYOUT_MODE) -> {
                Mode.valueOf(relationship.properties[C4_LAYOUT_MODE]!!)
            }

            view is DynamicView -> Mode.RelIndex
            else -> Mode.Rel
        }
        var relationshipMacro = mode.macro
        when (mode) {
            Mode.Rel, Mode.RelIndex -> {
                var direction = Direction.Down
                if (relationship.properties.containsKey(C4_LAYOUT_DIRECTION)) {
                    direction = Direction.valueOf(relationship.properties[C4_LAYOUT_DIRECTION]!!)
                    if (relationshipView.isResponse == true) {
                        direction = direction.inverse()
                    }
                }
                relationshipMacro = "${relationshipMacro}_${direction.macro()}"
            }

            else -> relationshipMacro = "Rel_$relationshipMacro"
        }
        val description = if (view is DynamicView) {
            relationshipView.description
        } else {
            relationship.description
        }.ifEmpty { " " }

        var relMacro = if (view is DynamicView) {
            """$relationshipMacro(${relationshipView.order},${idOf(source)}, ${idOf(destination)}, "$description""""
        } else {
            """$relationshipMacro(${idOf(source)}, ${idOf(destination)}, "$description""""
        }
        if (relationship.technology != null) {
            relMacro += """, "${relationship.technology}""""
        }
        val sprite = IconRegistry.iconFileNameFor(relationship.icon ?: "")
        if (!sprite.isNullOrBlank()) {
            relMacro += """, ${'$'}sprite=$sprite"""
        }
        if (!relationship.link.isNullOrBlank()) {
            relMacro += linkString(relationship.link)
        }
        if (relationship.interactionStyle == InteractionStyle.Asynchronous) {
            relMacro += """, ${'$'}tags="$ASYNC_REL_TAG_NAME""""
        }
        writer.writeLine("$relMacro)")
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
