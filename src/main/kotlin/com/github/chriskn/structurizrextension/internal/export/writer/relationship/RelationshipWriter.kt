package com.github.chriskn.structurizrextension.internal.export.writer.relationship

import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.api.model.icon
import com.github.chriskn.structurizrextension.api.model.link
import com.github.chriskn.structurizrextension.api.view.dynamic.renderAsSequenceDiagram
import com.github.chriskn.structurizrextension.api.view.layout.DependencyConfiguration
import com.github.chriskn.structurizrextension.api.view.layout.LayoutRegistry
import com.github.chriskn.structurizrextension.api.view.layout.Mode
import com.github.chriskn.structurizrextension.internal.export.idOf
import com.github.chriskn.structurizrextension.internal.export.writer.PropertyWriter
import com.github.chriskn.structurizrextension.internal.export.writer.linkString
import com.structurizr.export.IndentingWriter
import com.structurizr.model.InteractionStyle
import com.structurizr.view.DynamicView
import com.structurizr.view.ModelView
import com.structurizr.view.RelationshipView

private const val ASYNC_REL_TAG_NAME = "async relationship"

internal class RelationshipWriter(
    private val propertyWriter: PropertyWriter,
) {

    private val relationshipViewComparator = RelationshipViewComparator()
    private val dynamicViewRelationshipViewComparator = DynamicViewRelationshipViewComparator()

    fun writeRelationships(view: ModelView, writer: IndentingWriter) {
        val dependencyConfigurations = LayoutRegistry.layoutForKey(view.key).dependencyConfigurations
        val configurationsByRelationship: Map<RelationshipView, List<DependencyConfiguration>> = view
            .relationships
            .associateWith { relView ->
                dependencyConfigurations.filter { depConf ->
                    depConf.filter(relView.relationship)
                }
            }

        val sortedRelViews = if (view is DynamicView) {
            view.relationships.sortedWith(dynamicViewRelationshipViewComparator)
        } else {
            view.relationships.sortedWith(relationshipViewComparator)
        }

        sortedRelViews.forEach { relView: RelationshipView ->
            writeRelationship(
                view = view,
                relationshipView = relView,
                configurations = configurationsByRelationship.getOrDefault(relView, emptyList()),
                writer = writer
            )
        }
    }

    internal fun writeRelationship(
        view: ModelView,
        relationshipView: RelationshipView,
        configurations: List<DependencyConfiguration>,
        writer: IndentingWriter,
    ) {
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
        val description = determineDescription(view, relationshipView)
        val mode = determineMode(view, configurations)
        val relationshipType = determineType(mode, configurations, relationshipView)

        val relationshipBuilder = StringBuilder()
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

    private fun writeRelationshipSequenceDiagram(
        view: ModelView,
        relationshipView: RelationshipView,
        writer: IndentingWriter,
    ) {
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

    private fun determineDescription(
        view: ModelView,
        relationshipView: RelationshipView,
    ): String = if (view is DynamicView) {
        relationshipView.description
    } else {
        relationshipView.relationship.description
    }.ifEmpty { " " }

    private fun determineMode(
        view: ModelView,
        configurations: List<DependencyConfiguration>,
    ): Mode = if (view is DynamicView && !view.renderAsSequenceDiagram) {
        // Dynamic views use only indexed relationships
        Mode.RelIndex
    } else {
        configurations.map { it.mode }.lastOrNull() ?: Mode.Rel
    }

    private fun determineType(
        mode: Mode,
        configurations: List<DependencyConfiguration>,
        relationshipView: RelationshipView,
    ): String {
        val configuredDirection = configurations.map { it.direction }.lastOrNull()
        return when (mode) {
            Mode.Rel, Mode.RelIndex -> {
                if (configuredDirection != null) {
                    val direction = if (relationshipView.isResponse == true) {
                        configuredDirection.inverse()
                    } else {
                        configuredDirection
                    }
                    "${mode.macro}_${direction.macro()}"
                } else {
                    mode.macro
                }
            }

            else -> "Rel_${mode.macro}"
        }
    }
}
