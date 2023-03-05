package com.github.chriskn.structurizrextension.export.writer

import com.github.chriskn.structurizrextension.export.idOf
import com.github.chriskn.structurizrextension.model.icon
import com.github.chriskn.structurizrextension.model.link
import com.github.chriskn.structurizrextension.plantuml.IconRegistry
import com.structurizr.export.IndentingWriter
import com.structurizr.model.Container
import com.structurizr.model.DeploymentNode
import com.structurizr.model.SoftwareSystem

object BoundaryWriter {

    private var groupId = 0

    internal fun startEnterpriseBoundary(enterpriseName: String?, writer: IndentingWriter) {
        writer.writeLine("Enterprise_Boundary(enterprise, $enterpriseName) {")
        writer.indent()
    }

    internal fun endEnterpriseBoundary(writer: IndentingWriter) {
        writer.outdent()
        writer.writeLine("}")
        writer.writeLine()
    }

    internal fun startGroupBoundary(group: String?, writer: IndentingWriter) {
        writer.writeLine("Boundary(group_${groupId++}, $group) {")
        writer.indent()
    }

    internal fun endGroupBoundary(writer: IndentingWriter) {
        writer.outdent()
        writer.writeLine("}")
        writer.writeLine()
    }

    internal fun startSoftwareSystemBoundary(
        softwareSystem: SoftwareSystem,
        writer: IndentingWriter
    ) {
        writer.writeLine("System_Boundary(${idOf(softwareSystem)}, ${softwareSystem.name}) {")
        writer.indent()
    }

    internal fun endSoftwareSystemBoundary(writer: IndentingWriter) {
        writer.outdent()
        writer.writeLine("}")
    }

    internal fun startContainerBoundary(container: Container, writer: IndentingWriter) {
        writer.writeLine("""Container_Boundary("${idOf(container)}_boundary", "${container.name}") {""")
        writer.indent()
    }

    internal fun endContainerBoundary(writer: IndentingWriter) {
        writer.outdent()
        writer.writeLine("}")
    }

    internal fun startDeploymentNodeBoundary(
        deploymentNode: DeploymentNode,
        writer: IndentingWriter
    ) {
        writer.writeLine(deploymentNode.toMacro())
        writer.indent()
    }

    internal fun endDeploymentNodeBoundary(writer: IndentingWriter) {
        writer.outdent()
        writer.writeLine("}")
    }

    private fun DeploymentNode.toMacro() =
        """Node(${idOf(this)}, "$name", "${technology ?: ""}", "${description ?: ""}", "${
        IconRegistry.iconFileNameFor(
            icon
        ) ?: ""
        }"${linkString(link)}){"""
}
