package com.github.chriskn.structurizrextension.export.exporter

import com.github.chriskn.structurizrextension.export.createC4Diagram
import com.github.chriskn.structurizrextension.export.idOf
import com.github.chriskn.structurizrextension.export.writer.BoundaryWriter
import com.github.chriskn.structurizrextension.export.writer.ElementWriter
import com.github.chriskn.structurizrextension.export.writer.FooterWriter
import com.github.chriskn.structurizrextension.export.writer.HeaderWriter
import com.github.chriskn.structurizrextension.export.writer.RelationshipWriter
import com.structurizr.export.Diagram
import com.structurizr.export.IndentingWriter
import com.structurizr.model.Container
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ContainerView
import com.structurizr.view.ElementView
import com.structurizr.view.ModelView
import com.structurizr.view.showExternalSoftwareSystemBoundaries

class ContainerViewExporter(
    private val boundaryWriter: BoundaryWriter,
    private val footerWriter: FooterWriter,
    private val headerWriter: HeaderWriter,
    private val elementWriter: ElementWriter,
    private val relationshipWriter: RelationshipWriter
) {

    internal fun exportContainerView(view: ContainerView): Diagram {
        val writer = IndentingWriter()
        headerWriter.writeHeader(view, writer)

        val sortedElements = view.elements.sortedBy { idOf(it.element) }

        val boundarySoftwareSystems: List<ElementView> = if (view.showExternalSoftwareSystemBoundaries) {
            writeElementsInSoftwareSystemBoundaries(view, writer, sortedElements)
        } else {
            emptyList()
        }

        writeElementsOutsideBoundaries(
            elementsOutsideBoundaries = sortedElements - boundarySoftwareSystems.toSet(),
            view,
            writer
        )

        relationshipWriter.writeRelationships(view, writer)
        footerWriter.writeFooter(view, writer)

        return createC4Diagram(view, writer.toString())
    }

    private fun writeElementsOutsideBoundaries(
        elementsOutsideBoundaries: List<ElementView>,
        view: ContainerView,
        writer: IndentingWriter
    ) {
        for (elementView in elementsOutsideBoundaries) {
            elementWriter.writeElement(view, elementView.element, writer)
        }
        if (elementsOutsideBoundaries.isNotEmpty()) {
            writer.writeLine()
        }
    }

    private fun writeElementsInSoftwareSystemBoundaries(
        view: ContainerView,
        writer: IndentingWriter,
        sortedElements: List<ElementView>
    ): List<ElementView> {
        val boundarySoftwareSystems = getBoundarySoftwareSystemViews(view)
        for (softwareSystem in boundarySoftwareSystems) {
            boundaryWriter.startSoftwareSystemBoundary(softwareSystem, writer, view)
            for (elementView in sortedElements) {
                if (elementView.element.parent === softwareSystem) {
                    elementWriter.writeElement(view, elementView.element, writer)
                }
            }
            boundaryWriter.endSoftwareSystemBoundary(writer, view)
        }
        return view.elements.filter { it.element.parent in boundarySoftwareSystems }
    }

    private fun getBoundarySoftwareSystemViews(view: ModelView): List<SoftwareSystem> = view.elements
        .asSequence()
        .map { it.element }
        .filterIsInstance<Container>()
        .map { it.softwareSystem }
        .toSet()
        .sortedBy { idOf(it) }
}
