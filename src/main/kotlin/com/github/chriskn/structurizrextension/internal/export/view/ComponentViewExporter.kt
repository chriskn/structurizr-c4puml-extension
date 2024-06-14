package com.github.chriskn.structurizrextension.internal.export.view

import com.github.chriskn.structurizrextension.api.view.showExternalContainerBoundaries
import com.github.chriskn.structurizrextension.internal.export.ExtendedC4PlantUMLExporter
import com.github.chriskn.structurizrextension.internal.export.idOf
import com.structurizr.export.Diagram
import com.structurizr.export.IndentingWriter
import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.view.ComponentView
import com.structurizr.view.ElementView
import com.structurizr.view.ModelView

internal class ComponentViewExporter(
    private val c4PlantUMLExporter: ExtendedC4PlantUMLExporter,

) {

    fun exportComponentView(view: ComponentView): Diagram {
        val writer = IndentingWriter()
        c4PlantUMLExporter.writeHeader(view, writer)

        val sortedElements = view.elements.sortedBy { idOf(it.element) }

        val boundaryContainers: List<ElementView> = if (view.showExternalContainerBoundaries) {
            writeElementsInContainerBoundaries(view, writer, sortedElements)
        } else {
            emptyList()
        }

        writeElementsOutsideBoundaries(
            elementsOutsideBoundaries = sortedElements - boundaryContainers.toSet(),
            view,
            writer
        )

        c4PlantUMLExporter.writeRelationships(view, writer)
        c4PlantUMLExporter.writeFooter(view, writer)

        return c4PlantUMLExporter.createDiagram(view, writer.toString())
    }

    private fun writeElementsOutsideBoundaries(
        elementsOutsideBoundaries: List<ElementView>,
        view: ComponentView,
        writer: IndentingWriter
    ) {
        for (elementView in elementsOutsideBoundaries) {
            c4PlantUMLExporter.writeElement(view, elementView.element, writer)
        }
        if (elementsOutsideBoundaries.isEmpty()) {
            writer.writeLine()
        }
    }

    private fun writeElementsInContainerBoundaries(
        view: ComponentView,
        writer: IndentingWriter,
        sortedElements: List<ElementView>
    ): List<ElementView> {
        val boundaryContainers = getBoundaryContainer(view)
        for (container in boundaryContainers) {
            c4PlantUMLExporter.startContainerBoundary(view, container, writer)

            for (elementView in sortedElements) {
                if (elementView.element.parent === container) {
                    c4PlantUMLExporter.writeElement(view, elementView.element, writer)
                }
            }
            c4PlantUMLExporter.endContainerBoundary(view, writer)
        }
        return view.elements.filter { it.element.parent in boundaryContainers }
    }

    private fun getBoundaryContainer(view: ModelView): List<Container> =
        view.elements
            .asSequence()
            .map { obj: ElementView -> obj.element }
            .filterIsInstance<Component>()
            .map { it.container }
            .toSet()
            .sortedBy { idOf(it) }
}
