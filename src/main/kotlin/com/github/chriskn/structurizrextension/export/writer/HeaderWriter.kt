package com.github.chriskn.structurizrextension.export.writer

import com.github.chriskn.structurizrextension.model.icon
import com.github.chriskn.structurizrextension.plantuml.AWS_ICON_COMMONS
import com.github.chriskn.structurizrextension.plantuml.AWS_ICON_URL
import com.github.chriskn.structurizrextension.plantuml.IconRegistry
import com.github.chriskn.structurizrextension.view.LayoutRegistry
import com.github.chriskn.structurizrextension.view.renderAsSequenceDiagram
import com.structurizr.export.IndentingWriter
import com.structurizr.model.DeploymentNode
import com.structurizr.model.InteractionStyle
import com.structurizr.model.ModelItem
import com.structurizr.view.ComponentView
import com.structurizr.view.ContainerView
import com.structurizr.view.DeploymentView
import com.structurizr.view.DynamicView
import com.structurizr.view.ModelView
import com.structurizr.view.SystemContextView
import com.structurizr.view.SystemLandscapeView
import com.structurizr.view.View
import java.net.URI

object HeaderWriter {

    private const val ASYNC_REL_TAG_NAME = "async relationship"
    private const val C4_PLANT_UML_STDLIB_URL =
        "https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master"

    private val includes = mutableSetOf<URI>()

    internal fun writeHeader(view: ModelView, writer: IndentingWriter) {
        includes.clear()
        addIncludeUrls(view)
        // Spaces in PlantUML ids can cause issues. Alternatively, id can be surrounded with double quotes
        writer.writeLine("@startuml(id=${view.key.replace(' ', '_')})")
        for (include in includes) {
            writer.writeLine("!includeurl $include")
        }
        var viewTitle = view.title
        if (viewTitle.isNullOrBlank()) {
            viewTitle = view.name
        }
        writer.writeLine("title $viewTitle")
        if (!view.description.isNullOrBlank()) {
            writer.writeLine("caption " + view.description)
            writer.writeLine()
        }
        val layout = LayoutRegistry.layoutForKey(view.key)
        if (layout.showPersonOutline) {
            writer.writeLine("SHOW_PERSON_OUTLINE()")
        }
        // sequence diagrams do not support layout
        if (!(view is DynamicView && view.renderAsSequenceDiagram)) {
            writer.writeLine(layout.layout.macro)
        }
        writer.writeLine()
        if (view.relationships.any { it.relationship.interactionStyle == InteractionStyle.Asynchronous }) {
            writeAsyncRelTag(writer)
        }
    }

    private fun addIncludeUrls(view: View) {
        val elements: MutableSet<ModelItem> = (view as ModelView).elements.map { it.element }.toMutableSet()
        if (view is DeploymentView) {
            val children = elements
                .filterIsInstance<DeploymentNode>()
                .flatMap { collectElements(it, elements) }
            elements.addAll(children)
        }
        elements += view.relationships.map { it.relationship }
        val spriteIncludesForElements = elements
            .asSequence()
            .mapNotNull { it.icon?.let { technology -> IconRegistry.iconUrlFor(technology) } }
            .toSet()
            .toList()
            .sorted()
            .toMutableList()
        if (spriteIncludesForElements.any { it.startsWith(AWS_ICON_URL) }) {
            spriteIncludesForElements.add(0, AWS_ICON_COMMONS)
        }
        spriteIncludesForElements.forEach { includes.add(URI(it)) }
        val c4PumlIncludeURI = URI("$C4_PLANT_UML_STDLIB_URL/${includeForView(view)}")
        includes.add(c4PumlIncludeURI)
    }

    @Suppress("MaxLineLength")
    private fun writeAsyncRelTag(writer: IndentingWriter) {
        writer.writeLine(
            """AddRelTag("$ASYNC_REL_TAG_NAME", ${'$'}textColor="${'$'}ARROW_COLOR", ${'$'}lineColor="${'$'}ARROW_COLOR", ${'$'}lineStyle = DashedLine())"""
        )
        writer.writeLine()
    }

    private fun collectElements(
        deploymentNode: DeploymentNode,
        elements: MutableSet<ModelItem>
    ): MutableSet<ModelItem> {
        elements.addAll(deploymentNode.infrastructureNodes)
        elements.addAll(deploymentNode.softwareSystemInstances.map { it.softwareSystem })
        elements.addAll(deploymentNode.containerInstances.map { it.container })
        deploymentNode.children.forEach { collectElements(it, elements) }
        return elements
    }

    private fun includeForView(view: View) = when (view) {
        is DynamicView -> {
            if (view.renderAsSequenceDiagram) {
                "C4_Sequence.puml"
            } else {
                "C4_Dynamic.puml"
            }
        }

        is DeploymentView -> "C4_Deployment.puml"
        is ComponentView -> "C4_Component.puml"
        is ContainerView -> "C4_Container.puml"
        is SystemLandscapeView -> "C4_Context.puml"
        is SystemContextView -> "C4_Context.puml"
        else -> throw IllegalArgumentException("Unsupported view of class ${view::class.java.simpleName}")
    }
}
