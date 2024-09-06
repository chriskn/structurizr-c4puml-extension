package com.github.chriskn.structurizrextension.internal.export.writer

import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.api.model.icon
import com.github.chriskn.structurizrextension.api.model.link
import com.github.chriskn.structurizrextension.api.view.dynamic.renderAsSequenceDiagram
import com.github.chriskn.structurizrextension.internal.export.idOf
import com.structurizr.export.IndentingWriter
import com.structurizr.model.Container
import com.structurizr.model.DeploymentNode
import com.structurizr.model.ModelItem
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.DynamicView
import com.structurizr.view.View

@Suppress("TooManyFunctions")
internal object BoundaryWriter {

    private var groupId = 0

    fun startEnterpriseBoundary(view: View, enterpriseName: String, writer: IndentingWriter) {
        writer.writeLine("Enterprise_Boundary(enterprise, $enterpriseName) ${determineBoundaryStartSymbol(view)}")
        writer.indent()
    }

    fun endEnterpriseBoundary(writer: IndentingWriter, view: View) {
        writer.outdent()
        writer.writeLine(determineBoundaryEndSymbol(view))
        writer.writeLine()
    }

    fun startGroupBoundary(view: View, group: String?, writer: IndentingWriter) {
        writer.writeLine("Boundary(group_${groupId++}, $group) ${determineBoundaryStartSymbol(view)}")
        writer.indent()
    }

    fun endGroupBoundary(view: View, writer: IndentingWriter) {
        writer.outdent()
        writer.writeLine(determineBoundaryEndSymbol(view))
        writer.writeLine()
    }

    fun startSoftwareSystemBoundary(
        view: View,
        softwareSystem: SoftwareSystem,
        writer: IndentingWriter
    ) {
        writer.writeLine(
            "System_Boundary(${idOf(
                softwareSystem
            )}, ${softwareSystem.name}${tagsToPlantUmlSting(softwareSystem)}) ${determineBoundaryStartSymbol(view)} "
        )
        writer.indent()
    }

    fun endSoftwareSystemBoundary(view: View, writer: IndentingWriter) {
        writer.outdent()
        writer.writeLine(determineBoundaryEndSymbol(view))
    }

    fun startContainerBoundary(view: View, container: Container, writer: IndentingWriter) {
        writer.writeLine(
            """Container_Boundary("${
                idOf(container)
            }_boundary", "${container.name}" ${tagsToPlantUmlSting(container)})${determineBoundaryStartSymbol(view)} """
        )
        writer.indent()
    }

    fun endContainerBoundary(view: View, writer: IndentingWriter) {
        writer.outdent()
        writer.writeLine(determineBoundaryEndSymbol(view))
    }

    fun startDeploymentNodeBoundary(
        view: View,
        deploymentNode: DeploymentNode,
        writer: IndentingWriter
    ) {
        writer.writeLine("${deploymentNode.toMacro()} ${determineBoundaryStartSymbol(view)}")
        writer.indent()
    }

    fun endDeploymentNodeBoundary(view: View, writer: IndentingWriter) {
        writer.outdent()
        writer.writeLine(determineBoundaryEndSymbol(view))
    }

    private fun DeploymentNode.toMacro() =
        """Node(${idOf(this)}, "$name", "${technology.orEmpty()}", "${description.orEmpty()}", "${
            IconRegistry.iconFileNameFor(icon).orEmpty()
        }"${tagsToPlantUmlSting(this)}${linkString(link)})"""

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
