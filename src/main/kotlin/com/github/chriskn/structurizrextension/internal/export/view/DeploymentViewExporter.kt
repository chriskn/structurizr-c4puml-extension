package com.github.chriskn.structurizrextension.internal.export.view

import com.github.chriskn.structurizrextension.internal.export.ExtendedC4PlantUMLExporter
import com.github.chriskn.structurizrextension.internal.export.writer.PropertyWriter
import com.structurizr.export.Diagram
import com.structurizr.export.IndentingWriter
import com.structurizr.model.ContainerInstance
import com.structurizr.model.DeploymentNode
import com.structurizr.model.InfrastructureNode
import com.structurizr.model.SoftwareSystemInstance
import com.structurizr.view.DeploymentView
import com.structurizr.view.ElementView

internal class DeploymentViewExporter(
    private val c4PlantUMLExporter: ExtendedC4PlantUMLExporter,
    private val propertyWriter: PropertyWriter
) {
    fun exportDeploymentView(view: DeploymentView): Diagram {
        val writer = IndentingWriter()
        c4PlantUMLExporter.writeHeader(view, writer)

        view.elements
            .filter { ev: ElementView ->
                ev.element is DeploymentNode && ev.element.parent == null
            }
            .map { ev: ElementView -> ev.element as DeploymentNode }
            .sortedBy { it.name }
            .forEach { writeDeploymentNodeRecursive(view, it, writer) }

        c4PlantUMLExporter.writeRelationships(view, writer)
        c4PlantUMLExporter.writeFooter(view, writer)
        return c4PlantUMLExporter.createDiagram(view, writer.toString())
    }

    private fun writeDeploymentNodeRecursive(
        view: DeploymentView,
        deploymentNode: DeploymentNode,
        writer: IndentingWriter
    ) {
        if (!view.isElementInView(deploymentNode)) {
            return
        }
        propertyWriter.writeProperties(deploymentNode, writer)
        c4PlantUMLExporter.startDeploymentNodeBoundary(view, deploymentNode, writer)
        val children: List<DeploymentNode> = deploymentNode
            .children
            .toList()
            .sortedBy { node: DeploymentNode -> node.name }
        for (child in children) {
            if (view.isElementInView(child)) {
                writeDeploymentNodeRecursive(view, child, writer)
            }
        }
        val infrastructureNodes: List<InfrastructureNode> = deploymentNode
            .infrastructureNodes
            .toList()
            .sortedBy { node: InfrastructureNode -> node.name }
        for (infrastructureNode in infrastructureNodes) {
            if (view.isElementInView(infrastructureNode)) {
                c4PlantUMLExporter.writeElement(view, infrastructureNode, writer)
            }
        }
        val softwareSystemInstances: List<SoftwareSystemInstance> = deploymentNode
            .softwareSystemInstances
            .toList()
            .sortedBy { obj: SoftwareSystemInstance -> obj.name }
        for (softwareSystemInstance in softwareSystemInstances) {
            if (view.isElementInView(softwareSystemInstance)) {
                c4PlantUMLExporter.writeElement(view, softwareSystemInstance, writer)
            }
        }
        val containerInstances: List<ContainerInstance> = deploymentNode
            .containerInstances
            .toList()
            .sortedBy { obj: ContainerInstance -> obj.name }
        for (containerInstance in containerInstances) {
            if (view.isElementInView(containerInstance)) {
                c4PlantUMLExporter.writeElement(view, containerInstance, writer)
            }
        }
        c4PlantUMLExporter.endDeploymentNodeBoundary(view, writer)
    }
}
