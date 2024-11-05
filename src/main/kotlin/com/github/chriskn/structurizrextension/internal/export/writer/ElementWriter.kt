package com.github.chriskn.structurizrextension.internal.export.writer

import com.github.chriskn.structurizrextension.api.model.c4Location
import com.github.chriskn.structurizrextension.api.model.c4Type
import com.github.chriskn.structurizrextension.api.model.link
import com.github.chriskn.structurizrextension.internal.export.idOf
import com.github.chriskn.structurizrextension.internal.export.writer.StyleWriter.toPlantUmlString
import com.structurizr.export.IndentingWriter
import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.ContainerInstance
import com.structurizr.model.DeploymentElement
import com.structurizr.model.Element
import com.structurizr.model.InfrastructureNode
import com.structurizr.model.Location
import com.structurizr.model.ModelItem
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.model.SoftwareSystemInstance
import com.structurizr.view.ModelView
import com.structurizr.view.View
import mu.KotlinLogging

internal class ElementWriter(
    private val propertyWriter: PropertyWriter,
) {

    private val logger = KotlinLogging.logger {}

    fun writeElement(view: ModelView, element: Element, writer: IndentingWriter) {
        propertyWriter.writeProperties(element, writer)
        when (element) {
            is DeploymentElement -> writeDeploymentElement(view, element, writer)
            is Container -> writer.writeLine(element.toMacro(idOf(element)))
            is SoftwareSystem -> writer.writeLine(element.toMacro(idOf(element)))
            is Person -> writer.writeLine(element.toMacro())
            is Component -> writer.writeLine(element.toMacro())
            else -> {
                logger.info { "Ignoring unknown element type ${element::class.java} with id ${idOf(element)}" }
                return
            }
        }
    }

    private fun writeDeploymentElement(
        view: View,
        deploymentElement: DeploymentElement,
        writer: IndentingWriter,
    ) {
        if ((view as ModelView).isElementInView(deploymentElement)) {
            when (deploymentElement) {
                is ContainerInstance -> {
                    propertyWriter.writeProperties(deploymentElement.container, writer)
                    writer.writeLine(deploymentElement.container.toMacro(idOf(deploymentElement)))
                }

                is SoftwareSystemInstance -> {
                    propertyWriter.writeProperties(deploymentElement.softwareSystem, writer)
                    writer.writeLine(deploymentElement.softwareSystem.toMacro(idOf(deploymentElement)))
                }

                is InfrastructureNode -> {
                    writer.writeLine(deploymentElement.toMacro())
                }
            }
        }
    }

    private fun InfrastructureNode.toMacro(): String {
        val usedSprite = this.getUsedIconOrSprite()
        return """Node(${idOf(this)}, "$name", "${technology.orEmpty()}", "${
            description.orEmpty()}", $usedSprite${tagsToPlantUmlSting(this)}${linkString(link)})"""
    }

    private fun SoftwareSystem.toMacro(id: String): String {
        val usedSprite = this.getUsedIconOrSprite()
        return """System${this.c4Type?.c4Type.orEmpty()}${this.c4Location.toPlantUmlString()}($id, "$name", "${
            description.orEmpty()}", $usedSprite${tagsToPlantUmlSting(this)}${linkString(link)})"""
    }

    private fun Container.toMacro(id: String): String {
        val usedSprite = this.getUsedIconOrSprite()
        return """Container${this.c4Type?.c4Type.orEmpty()}${
            this.c4Location.toPlantUmlString()}($id, "$name", "$technology", "${
            description.orEmpty()}", $usedSprite${tagsToPlantUmlSting(this)}${linkString(link)})"""
    }

    private fun Person.toMacro(): String {
        val usedSprite = this.getUsedIconOrSprite()
        val externalMarker = this.c4Location.toPlantUmlString()
        return """Person$externalMarker(${idOf(this)}, "$name", "${description.orEmpty()}", $usedSprite${
            tagsToPlantUmlSting(this)}${linkString(link)})"""
    }

    private fun Component.toMacro(): String {
        val usedSprite = this.getUsedIconOrSprite()

        return """Component${this.c4Type?.c4Type.orEmpty()}(${
            idOf(this)
        }, "$name", "${technology.orEmpty()}", "${description.orEmpty()}", $usedSprite${
            tagsToPlantUmlSting(this)}${linkString(link)})"""
    }

    private fun Location.toPlantUmlString() = if (this == Location.External) "_Ext" else ""

    private fun tagsToPlantUmlSting(modelItem: ModelItem): String {
        // dont add structurizr default tags
        val tagList = modelItem.tagsAsSet - modelItem.defaultTags
        return if (tagList.isEmpty()) {
            ""
        } else {
            """, ${'$'}tags="${tagList.joinToString("+")}""""
        }
    }
}
