package com.github.chriskn.structurizrextension.internal.export.view

import com.github.chriskn.structurizrextension.api.view.showExternalSoftwareSystemBoundaries
import com.github.chriskn.structurizrextension.internal.export.ExtendedC4PlantUMLExporter
import com.github.chriskn.structurizrextension.internal.export.idOf
import com.structurizr.export.Diagram
import com.structurizr.export.IndentingWriter
import com.structurizr.model.Container
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ContainerView
import com.structurizr.view.ElementView
import com.structurizr.view.ModelView

internal class ContainerViewExporter(
    private val c4PlantUMLExporter: ExtendedC4PlantUMLExporter,
) {

    fun exportContainerView(view: ContainerView): Diagram {
        val writer = IndentingWriter()
        c4PlantUMLExporter.writeHeader(view, writer)

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

        c4PlantUMLExporter.writeRelationships(view, writer)
        c4PlantUMLExporter.writeFooter(view, writer)

        return c4PlantUMLExporter.createDiagram(view, writer.toString())
    }

    private fun writeElementsOutsideBoundaries(
        elementsOutsideBoundaries: List<ElementView>,
        view: ContainerView,
        writer: IndentingWriter
    ) {
        for (elementView in elementsOutsideBoundaries) {
            c4PlantUMLExporter.writeElement(view, elementView.element, writer)
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
            c4PlantUMLExporter.startSoftwareSystemBoundary(view, softwareSystem, writer)
            for (elementView in sortedElements) {
                if (elementView.element.parent === softwareSystem) {
                    c4PlantUMLExporter.writeElement(view, elementView.element, writer)
                }
            }
            c4PlantUMLExporter.endSoftwareSystemBoundary(view, writer)
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
