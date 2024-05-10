package com.github.chriskn.structurizrextension.export.exporter

import com.github.chriskn.structurizrextension.export.createC4Diagram
import com.github.chriskn.structurizrextension.export.writer.BoundaryWriter
import com.github.chriskn.structurizrextension.export.writer.ElementWriter
import com.github.chriskn.structurizrextension.export.writer.FooterWriter
import com.github.chriskn.structurizrextension.export.writer.HeaderWriter
import com.github.chriskn.structurizrextension.export.writer.RelationshipWriter
import com.structurizr.export.Diagram
import com.structurizr.export.IndentingWriter
import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.view.ComponentView
import com.structurizr.view.ElementView
import com.structurizr.view.ModelView

class ComponentViewExporter(
    private val boundaryWriter: BoundaryWriter,
    private val footerWriter: FooterWriter,
    private val headerWriter: HeaderWriter,
    private val elementWriter: ElementWriter,
    private val relationshipWriter: RelationshipWriter
) {

    internal fun exportComponentView(view: ComponentView): Diagram {
        val writer = IndentingWriter()
        headerWriter.writeHeader(view, writer)

        var elementsWritten = false
        val sortedElements = view.elements.sortedBy { it.id }

        for (elementView in sortedElements) {
            if (elementView.element !is Component) {
                elementWriter.writeElement(view, elementView.element, writer)
                elementsWritten = true
            }
        }
        if (elementsWritten) {
            writer.writeLine()
        }

        val containers = getBoundaryContainer(view)
        for (container in containers) {
            val showContainerBoundary = view.externalContainerBoundariesVisible
            if (showContainerBoundary) {
                boundaryWriter.startContainerBoundary(container, writer, view)
            }
            for (elementView in sortedElements) {
                if (elementView.element.parent === container) {
                    elementWriter.writeElement(view, elementView.element, writer)
                }
            }
            if (showContainerBoundary) {
                boundaryWriter.endContainerBoundary(writer, view)
            } else {
                writer.writeLine()
            }
        }

        relationshipWriter.writeRelationships(view, writer)

        footerWriter.writeFooter(view, writer)

        return createC4Diagram(view, writer.toString())
    }

    private fun getBoundaryContainer(view: ModelView): List<Container> =
        view.elements
            .asSequence()
            .map { obj: ElementView -> obj.element }
            .filterIsInstance<Component>()
            .map { it.container }
            .toSet()
            .sortedBy { it.id }
}
