package com.github.chriskn.structurizrextension.export

import com.github.chriskn.structurizrextension.export.exporter.ComponentViewExporter
import com.github.chriskn.structurizrextension.export.exporter.ContainerViewExporter
import com.github.chriskn.structurizrextension.export.exporter.DeploymentViewExporter
import com.github.chriskn.structurizrextension.export.exporter.DynamicViewExporter
import com.github.chriskn.structurizrextension.export.exporter.SystemViewExporter
import com.github.chriskn.structurizrextension.export.writer.BoundaryWriter
import com.github.chriskn.structurizrextension.export.writer.ElementWriter
import com.github.chriskn.structurizrextension.export.writer.FooterWriter
import com.github.chriskn.structurizrextension.export.writer.HeaderWriter
import com.github.chriskn.structurizrextension.export.writer.PropertyWriter
import com.github.chriskn.structurizrextension.export.writer.RelationshipWriter
import com.structurizr.export.AbstractDiagramExporter
import com.structurizr.export.Diagram
import com.structurizr.export.IndentingWriter
import com.structurizr.model.Container
import com.structurizr.model.DeploymentNode
import com.structurizr.model.Element
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ComponentView
import com.structurizr.view.ContainerView
import com.structurizr.view.DeploymentView
import com.structurizr.view.DynamicView
import com.structurizr.view.ModelView
import com.structurizr.view.RelationshipView
import com.structurizr.view.SystemContextView
import com.structurizr.view.SystemLandscapeView

@Suppress("TooManyFunctions")
class ExtendedC4PlantUMLExporter : AbstractDiagramExporter() {

    private val boundaryWriter = BoundaryWriter
    private val footerWriter = FooterWriter
    private val headerWriter = HeaderWriter
    private val propertyWriter = PropertyWriter
    private val elementWriter = ElementWriter(propertyWriter)
    private val relationshipWriter = RelationshipWriter(propertyWriter)
    private val deploymentViewExporter = DeploymentViewExporter(
        boundaryWriter,
        footerWriter,
        headerWriter,
        propertyWriter,
        elementWriter,
        relationshipWriter
    )
    private val dynamicViewExporter = DynamicViewExporter(
        boundaryWriter,
        footerWriter,
        headerWriter,
        elementWriter,
        relationshipWriter
    )
    private val containerViewExporter = ContainerViewExporter(
        boundaryWriter,
        footerWriter,
        headerWriter,
        elementWriter,
        relationshipWriter
    )
    private val componentViewExporter = ComponentViewExporter(
        boundaryWriter,
        footerWriter,
        headerWriter,
        elementWriter,
        relationshipWriter
    )
    private val systemViewExporter = SystemViewExporter(
        boundaryWriter,
        footerWriter,
        headerWriter,
        elementWriter,
        relationshipWriter
    )

    override fun writeHeader(view: ModelView, writer: IndentingWriter) {
        headerWriter.writeHeader(view, writer)
    }

    override fun writeElement(view: ModelView, element: Element, writer: IndentingWriter) {
        elementWriter.writeElement(view, element, writer)
    }

    override fun writeRelationships(view: ModelView, writer: IndentingWriter) {
        relationshipWriter.writeRelationships(view, writer)
    }

    override fun writeRelationship(view: ModelView, relationshipView: RelationshipView, writer: IndentingWriter) {
        relationshipWriter.writeRelationship(view, relationshipView, writer)
    }

    override fun writeFooter(view: ModelView, writer: IndentingWriter) {
        footerWriter.writeFooter(view, writer)
    }

    override fun export(view: DynamicView, order: String?): Diagram = dynamicViewExporter.exportDynamicView(view)

    override fun export(view: DeploymentView): Diagram = deploymentViewExporter.exportDeploymentView(view)

    override fun export(view: ContainerView): Diagram = containerViewExporter.exportContainerView(view)

    override fun export(view: ComponentView): Diagram = componentViewExporter.exportComponentView(view)

    override fun export(view: SystemLandscapeView): Diagram = systemViewExporter.exportLandscapeView(view)

    override fun export(view: SystemContextView): Diagram = systemViewExporter.exportContextView(view)

    override fun createDiagram(view: ModelView, definition: String): Diagram = createC4Diagram(view, definition)

    override fun startEnterpriseBoundary(view: ModelView, enterpriseName: String, writer: IndentingWriter) {
        boundaryWriter.startEnterpriseBoundary(enterpriseName, writer, view)
    }

    override fun endEnterpriseBoundary(view: ModelView, writer: IndentingWriter) {
        boundaryWriter.endEnterpriseBoundary(writer, view)
    }

    override fun startGroupBoundary(view: ModelView, group: String, writer: IndentingWriter) {
        boundaryWriter.startGroupBoundary(group, writer, view)
    }

    override fun endGroupBoundary(view: ModelView, writer: IndentingWriter) {
        boundaryWriter.endGroupBoundary(writer, view)
    }

    override fun startSoftwareSystemBoundary(view: ModelView, softwareSystem: SoftwareSystem, writer: IndentingWriter) {
        boundaryWriter.startSoftwareSystemBoundary(softwareSystem, writer, view)
    }

    override fun endSoftwareSystemBoundary(view: ModelView, writer: IndentingWriter) {
        boundaryWriter.endSoftwareSystemBoundary(writer, view)
    }

    override fun startContainerBoundary(view: ModelView, container: Container, writer: IndentingWriter) {
        boundaryWriter.startContainerBoundary(container, writer, view)
    }

    override fun endContainerBoundary(view: ModelView, writer: IndentingWriter) {
        boundaryWriter.endContainerBoundary(writer, view)
    }

    override fun startDeploymentNodeBoundary(
        view: DeploymentView,
        deploymentNode: DeploymentNode,
        writer: IndentingWriter
    ) {
        boundaryWriter.startDeploymentNodeBoundary(deploymentNode, writer, view)
    }

    override fun endDeploymentNodeBoundary(view: ModelView, writer: IndentingWriter) {
        boundaryWriter.endDeploymentNodeBoundary(writer, view)
    }
}
