package com.github.chriskn.structurizrextension.export.exporter

import com.github.chriskn.structurizrextension.export.createC4Diagram
import com.github.chriskn.structurizrextension.export.writer.BoundaryWriter
import com.github.chriskn.structurizrextension.export.writer.ElementWriter
import com.github.chriskn.structurizrextension.export.writer.FooterWriter
import com.github.chriskn.structurizrextension.export.writer.HeaderWriter
import com.github.chriskn.structurizrextension.export.writer.PropertyWriter
import com.github.chriskn.structurizrextension.export.writer.RelationshipWriter
import com.structurizr.export.Diagram
import com.structurizr.export.IndentingWriter
import com.structurizr.model.ContainerInstance
import com.structurizr.model.DeploymentNode
import com.structurizr.model.InfrastructureNode
import com.structurizr.model.SoftwareSystemInstance
import com.structurizr.view.DeploymentView
import com.structurizr.view.ElementView

class DeploymentViewExporter(
    private val boundaryWriter: BoundaryWriter,
    private val footerWriter: FooterWriter,
    private val headerWriter: HeaderWriter,
    private val propertyWriter: PropertyWriter,
    private val elementWriter: ElementWriter,
    private val relationshipWriter: RelationshipWriter
) {
    internal fun exportDeploymentView(view: DeploymentView): Diagram {
        val writer = IndentingWriter()
        headerWriter.writeHeader(view, writer)
        view.elements.stream()
            .filter { ev: ElementView ->
                ev.element is DeploymentNode && ev.element.parent == null
            }
            .map { ev: ElementView -> ev.element as DeploymentNode }
            .sorted(Comparator.comparing { obj: DeploymentNode -> obj.name })
            .forEach { e: DeploymentNode -> writeDeploymentNodeRecursive(view, e, writer) }
        relationshipWriter.writeRelationships(view, writer)
        footerWriter.writeFooter(view, writer)
        return createC4Diagram(view, writer.toString())
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
        boundaryWriter.startDeploymentNodeBoundary(deploymentNode, writer)
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
                elementWriter.writeElement(view, infrastructureNode, writer)
            }
        }
        val softwareSystemInstances: List<SoftwareSystemInstance> = deploymentNode
            .softwareSystemInstances
            .toList()
            .sortedBy { obj: SoftwareSystemInstance -> obj.name }
        for (softwareSystemInstance in softwareSystemInstances) {
            if (view.isElementInView(softwareSystemInstance)) {
                elementWriter.writeElement(view, softwareSystemInstance, writer)
            }
        }
        val containerInstances: List<ContainerInstance> = deploymentNode
            .containerInstances
            .toList()
            .sortedBy { obj: ContainerInstance -> obj.name }
        for (containerInstance in containerInstances) {
            if (view.isElementInView(containerInstance)) {
                elementWriter.writeElement(view, containerInstance, writer)
            }
        }
        boundaryWriter.endDeploymentNodeBoundary(writer)
    }
}
