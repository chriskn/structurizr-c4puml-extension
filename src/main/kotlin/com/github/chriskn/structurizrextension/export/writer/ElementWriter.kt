package com.github.chriskn.structurizrextension.export.writer

import com.github.chriskn.structurizrextension.export.idOf
import com.github.chriskn.structurizrextension.model.c4Type
import com.github.chriskn.structurizrextension.model.icon
import com.github.chriskn.structurizrextension.model.link
import com.github.chriskn.structurizrextension.model.location
import com.github.chriskn.structurizrextension.plantuml.IconRegistry
import com.structurizr.export.IndentingWriter
import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.ContainerInstance
import com.structurizr.model.DeploymentElement
import com.structurizr.model.Element
import com.structurizr.model.InfrastructureNode
import com.structurizr.model.Location
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.model.SoftwareSystemInstance
import com.structurizr.view.ModelView
import com.structurizr.view.View
import mu.KotlinLogging

class ElementWriter(
    private val propertyWriter: PropertyWriter
) {

    private val logger = KotlinLogging.logger {}

    internal fun writeElement(view: ModelView, element: Element, writer: IndentingWriter) {
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

    private fun InfrastructureNode.toMacro() =
        """Node(${idOf(this)}, "$name", "${technology ?: ""}", "${description ?: ""}", "${
        IconRegistry.iconFileNameFor(
            icon
        ) ?: ""
        }"${linkString(link)})"""

    private fun SoftwareSystem.toMacro(id: String) =
        """System${this.c4Type?.c4Type ?: ""}${this.location.toPlantUmlString()}($id, "$name", "${description ?: ""}", "${
        IconRegistry.iconFileNameFor(
            icon
        ) ?: ""
        }"${linkString(link)})"""

    private fun Container.toMacro(id: String): String =
        """Container${this.c4Type?.c4Type ?: ""}${this.location.toPlantUmlString()}($id, "$name", "$technology", "${description ?: ""}", "${
        IconRegistry.iconFileNameFor(
            icon
        ) ?: ""
        }"${linkString(link)})"""

    private fun Person.toMacro(): String {
        val externalMarker = this.location.toPlantUmlString()
        return """Person$externalMarker(${idOf(this)}, "$name", "${description ?: ""}", "${
        IconRegistry.iconFileNameFor(
            icon
        ) ?: ""
        }"${linkString(link)})"""
    }

    private fun Component.toMacro(): String {
        return """Component${this.c4Type?.c4Type ?: ""}(${idOf(this)}, "$name", "$technology", "${description ?: ""}", "${
        IconRegistry.iconFileNameFor(
            icon
        ) ?: ""
        }"${linkString(link)})"""
    }

    private fun Location.toPlantUmlString() = if (this == Location.External) "_Ext" else ""
}
