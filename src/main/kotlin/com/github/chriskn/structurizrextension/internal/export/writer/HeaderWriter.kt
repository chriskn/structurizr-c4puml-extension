package com.github.chriskn.structurizrextension.internal.export.writer

import com.github.chriskn.structurizrextension.api.model.sprite
import com.github.chriskn.structurizrextension.api.view.dynamic.renderAsSequenceDiagram
import com.github.chriskn.structurizrextension.api.view.sprite.sprites.PlantUmlSprite
import com.github.chriskn.structurizrextension.api.view.style.styles.BoundaryStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.DependencyStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.ElementStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.PersonStyle
import com.github.chriskn.structurizrextension.internal.view.layout.LayoutRegistry
import com.structurizr.export.IndentingWriter
import com.structurizr.model.DeploymentNode
import com.structurizr.model.ModelItem
import com.structurizr.view.ComponentView
import com.structurizr.view.ContainerView
import com.structurizr.view.DeploymentView
import com.structurizr.view.DynamicView
import com.structurizr.view.ModelView
import com.structurizr.view.SystemContextView
import com.structurizr.view.SystemLandscapeView
import com.structurizr.view.View

private const val C4_PLANT_UML_STDLIB_URL =
    "https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master"

internal class HeaderWriter(private val styleWriter: StyleWriter) {

    fun writeHeader(view: ModelView, writer: IndentingWriter) {
        // Spaces in PlantUML ids can cause issues. Alternatively, id can be surrounded with double quotes
        writer.writeLine("@startuml(id=${view.key.replace(' ', '_')})")

        val c4PumlIncludeURI = "$C4_PLANT_UML_STDLIB_URL/${includeForView(view)}"
        val dependencyStyles = styleWriter.collectAppliedDependencyStyles(view)
        val personStyles = styleWriter.collectAppliedPersonStyles(view)
        val boundaryStyles = styleWriter.collectAppliedBoundaryStyles(view)
        val elementsStyles = styleWriter.collectAppliedElementStyles(view)
        val modelElementStyles = elementsStyles + boundaryStyles + personStyles
        val sprites = (
            modelElementStyles.map { listOf(it.sprite, it.legendSprite) }.toList().flatten() +
                dependencyStyles.map { listOf(it.sprite, it.legendSprite) }.toList().flatten() +
                collectModelElements(view).map { it.sprite } +
                view.relationships.map { it.relationship.sprite }
            ).filterNotNull()
            .toSet()
            .toList()

        val spriteDefinitions = sprites.map { it.additionalDefinitions() }.flatten().toSortedSet()
        spriteDefinitions.forEach {
            writer.writeLine("!define $it")
        }

        val spriteIncludes = sprites.filterIsInstance<PlantUmlSprite>().map { it.path }.toSortedSet()
        val spriteAdditionalIncludes = sprites.map { it.additionalIncludes() }.flatten().toSortedSet().toList()
        val allSpriteIncludes = spriteAdditionalIncludes + spriteIncludes
        val includes = listOf(c4PumlIncludeURI) + allSpriteIncludes
        includes.forEach {
            writer.writeLine("!includeurl $it")
        }

        writer.writeLine()

        val viewTitle = if (!view.title.isNullOrBlank()) {
            view.title
        } else {
            view.name
        }
        writer.writeLine("title $viewTitle")
        if (!view.description.isNullOrBlank()) {
            writer.writeLine("caption " + view.description)
        }
        writer.writeLine()

        if (view is DeploymentView) {
            writer.writeLine("skinparam PackageTitleAlignment Center")
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

        writeStyles(dependencyStyles, writer, elementsStyles, boundaryStyles, personStyles)
    }

    private fun writeStyles(
        dependencyStyles: List<DependencyStyle>,
        writer: IndentingWriter,
        elementsStyles: List<ElementStyle>,
        boundaryStyles: List<BoundaryStyle>,
        personStyles: List<PersonStyle>,
    ) {
        dependencyStyles.forEach { style ->
            styleWriter.writeDependencyStyle(dependencyStyle = style, writer = writer)
        }
        elementsStyles.forEach { style ->
            styleWriter.writeElementStyle(elementStyle = style, writer = writer)
        }
        boundaryStyles.forEach { style ->
            styleWriter.writeModelElementStyle(modelElementStyle = style, writer = writer, tagType = "Boundary")
        }
        personStyles.forEach { style ->
            styleWriter.writeModelElementStyle(modelElementStyle = style, writer = writer, tagType = "Person")
        }
        if (dependencyStyles.size +
            elementsStyles.size +
            boundaryStyles.size +
            personStyles.size > 0
        ) {
            writer.writeLine()
        }
    }

    private fun collectModelElements(view: ModelView): MutableSet<ModelItem> {
        val elements: MutableSet<ModelItem> = view.elements.map { it.element }.toMutableSet()
        if (view is DeploymentView) {
            val children = elements
                .filterIsInstance<DeploymentNode>()
                .flatMap { collectElements(it, elements) }
            elements.addAll(children)
        }
        elements += view.relationships.map { it.relationship }
        return elements
    }

    private fun collectElements(
        deploymentNode: DeploymentNode,
        elements: MutableSet<ModelItem>,
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
