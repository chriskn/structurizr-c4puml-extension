package com.github.chriskn.structurizrextension.internal.export

import com.github.chriskn.structurizrextension.internal.export.view.ComponentViewExporter
import com.github.chriskn.structurizrextension.internal.export.view.ContainerViewExporter
import com.github.chriskn.structurizrextension.internal.export.view.DeploymentViewExporter
import com.github.chriskn.structurizrextension.internal.export.view.DynamicViewExporter
import com.github.chriskn.structurizrextension.internal.export.view.SystemViewExporter
import com.github.chriskn.structurizrextension.internal.export.writer.BoundaryWriter
import com.github.chriskn.structurizrextension.internal.export.writer.ElementWriter
import com.github.chriskn.structurizrextension.internal.export.writer.FooterWriter
import com.github.chriskn.structurizrextension.internal.export.writer.HeaderWriter
import com.github.chriskn.structurizrextension.internal.export.writer.PropertyWriter
import com.github.chriskn.structurizrextension.internal.export.writer.RelationshipWriter
import com.structurizr.export.AbstractDiagramExporter
import com.structurizr.export.Diagram
import com.structurizr.export.IndentingWriter
import com.structurizr.export.plantuml.PlantUMLDiagram
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
internal class ExtendedC4PlantUMLExporter : AbstractDiagramExporter() {

    private val boundaryWriter = BoundaryWriter
    private val footerWriter = FooterWriter
    private val headerWriter = HeaderWriter
    private val propertyWriter = PropertyWriter
    private val elementWriter = ElementWriter(propertyWriter)
    private val relationshipWriter = RelationshipWriter(propertyWriter)
    private val deploymentViewExporter = DeploymentViewExporter(this, propertyWriter)
    private val dynamicViewExporter = DynamicViewExporter(this)
    private val containerViewExporter = ContainerViewExporter(this)
    private val componentViewExporter = ComponentViewExporter(this)
    private val systemViewExporter = SystemViewExporter(this)

    public override fun writeHeader(view: ModelView, writer: IndentingWriter) {
        headerWriter.writeHeader(view, writer)
    }

    public override fun writeElement(view: ModelView, element: Element, writer: IndentingWriter) {
        elementWriter.writeElement(view, element, writer)
    }

    public override fun writeRelationships(view: ModelView, writer: IndentingWriter) {
        relationshipWriter.writeRelationships(view, writer)
    }

    override fun writeRelationship(view: ModelView, relationshipView: RelationshipView, writer: IndentingWriter) {
        relationshipWriter.writeRelationship(view, relationshipView, writer)
    }

    public override fun writeFooter(view: ModelView, writer: IndentingWriter) {
        footerWriter.writeFooter(view, writer)
    }

    public override fun createDiagram(view: ModelView, definition: String): Diagram = createC4Diagram(view, definition)

    override fun export(view: DynamicView, order: String?): Diagram = dynamicViewExporter.exportDynamicView(view)

    override fun export(view: DeploymentView): Diagram = deploymentViewExporter.exportDeploymentView(view)

    override fun export(view: ContainerView): Diagram = containerViewExporter.exportContainerView(view)

    override fun export(view: ComponentView): Diagram = componentViewExporter.exportComponentView(view)

    override fun export(view: SystemLandscapeView): Diagram = systemViewExporter.exportLandscapeView(view)

    override fun export(view: SystemContextView): Diagram = systemViewExporter.exportContextView(view)

    public override fun startEnterpriseBoundary(view: ModelView, enterpriseName: String, writer: IndentingWriter) {
        boundaryWriter.startEnterpriseBoundary(view, enterpriseName, writer)
    }

    public override fun endEnterpriseBoundary(view: ModelView, writer: IndentingWriter) {
        boundaryWriter.endEnterpriseBoundary(writer, view)
    }

    public override fun startGroupBoundary(view: ModelView, group: String, writer: IndentingWriter) {
        boundaryWriter.startGroupBoundary(view, group, writer)
    }

    public override fun endGroupBoundary(view: ModelView, writer: IndentingWriter) {
        boundaryWriter.endGroupBoundary(view, writer)
    }

    public override fun startSoftwareSystemBoundary(
        view: ModelView,
        softwareSystem: SoftwareSystem,
        writer: IndentingWriter
    ) {
        boundaryWriter.startSoftwareSystemBoundary(view, softwareSystem, writer)
    }

    public override fun endSoftwareSystemBoundary(view: ModelView, writer: IndentingWriter) {
        boundaryWriter.endSoftwareSystemBoundary(view, writer)
    }

    public override fun startContainerBoundary(view: ModelView, container: Container, writer: IndentingWriter) {
        boundaryWriter.startContainerBoundary(view, container, writer)
    }

    public override fun endContainerBoundary(view: ModelView, writer: IndentingWriter) {
        boundaryWriter.endContainerBoundary(view, writer)
    }

    public override fun startDeploymentNodeBoundary(
        view: DeploymentView,
        deploymentNode: DeploymentNode,
        writer: IndentingWriter
    ) {
        boundaryWriter.startDeploymentNodeBoundary(view, deploymentNode, writer)
    }

    public override fun endDeploymentNodeBoundary(view: ModelView, writer: IndentingWriter) {
        boundaryWriter.endDeploymentNodeBoundary(view, writer)
    }

    private fun createC4Diagram(view: ModelView, definition: String): Diagram {
        return PlantUMLDiagram(view, definition)
    }
}
