package com.github.chriskn.structurizrextension.export.writer

import com.github.chriskn.structurizrextension.export.idOf
import com.github.chriskn.structurizrextension.model.icon
import com.github.chriskn.structurizrextension.model.link
import com.github.chriskn.structurizrextension.plantuml.IconRegistry
import com.github.chriskn.structurizrextension.view.renderAsSequenceDiagram
import com.structurizr.export.IndentingWriter
import com.structurizr.model.Container
import com.structurizr.model.DeploymentNode
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.DynamicView
import com.structurizr.view.View

@Suppress("TooManyFunctions")
object BoundaryWriter {

    private var groupId = 0

    internal fun startEnterpriseBoundary(enterpriseName: String?, writer: IndentingWriter, view: View) {
        writer.writeLine("Enterprise_Boundary(enterprise, $enterpriseName) ${determineBoundaryStartSymbol(view)}")
        writer.indent()
    }

    internal fun endEnterpriseBoundary(writer: IndentingWriter, view: View) {
        writer.outdent()
        writer.writeLine(determineBoundaryEndSymbol(view))
        writer.writeLine()
    }

    internal fun startGroupBoundary(group: String?, writer: IndentingWriter, view: View) {
        writer.writeLine("Boundary(group_${groupId++}, $group) ${determineBoundaryStartSymbol(view)}")
        writer.indent()
    }

    internal fun endGroupBoundary(writer: IndentingWriter, view: View) {
        writer.outdent()
        writer.writeLine(determineBoundaryEndSymbol(view))
        writer.writeLine()
    }

    internal fun startSoftwareSystemBoundary(
        softwareSystem: SoftwareSystem,
        writer: IndentingWriter,
        view: View
    ) {
        writer.writeLine(
            "System_Boundary(${idOf(softwareSystem)}, ${softwareSystem.name}) ${determineBoundaryStartSymbol(view)}"
        )
        writer.indent()
    }

    internal fun endSoftwareSystemBoundary(writer: IndentingWriter, view: View) {
        writer.outdent()
        writer.writeLine(determineBoundaryEndSymbol(view))
    }

    internal fun startContainerBoundary(container: Container, writer: IndentingWriter, view: View) {
        writer.writeLine(
            """Container_Boundary("${idOf(
                container
            )}_boundary", "${container.name}") ${determineBoundaryStartSymbol(view)}"""
        )
        writer.indent()
    }

    internal fun endContainerBoundary(writer: IndentingWriter, view: View) {
        writer.outdent()
        writer.writeLine(determineBoundaryEndSymbol(view))
    }

    internal fun startDeploymentNodeBoundary(
        deploymentNode: DeploymentNode,
        writer: IndentingWriter,
        view: View
    ) {
        writer.writeLine("${deploymentNode.toMacro()} ${determineBoundaryStartSymbol(view)}")
        writer.indent()
    }

    internal fun endDeploymentNodeBoundary(writer: IndentingWriter, view: View) {
        writer.outdent()
        writer.writeLine(determineBoundaryEndSymbol(view))
    }

    private fun DeploymentNode.toMacro() =
        """Node(${idOf(this)}, "$name", "${technology ?: ""}", "${description ?: ""}", "${
            IconRegistry.iconFileNameFor(
                icon
            ) ?: ""
        }"${linkString(link)})"""

    private fun determineBoundaryEndSymbol(view: View): String =
        if (view is DynamicView && view.renderAsSequenceDiagram) {
            "Boundary_End()"
        } else {
            "}"
        }

    private fun determineBoundaryStartSymbol(view: View): String =
        if (view is DynamicView && view.renderAsSequenceDiagram) {
            ""
        } else {
            "{"
        }
}
