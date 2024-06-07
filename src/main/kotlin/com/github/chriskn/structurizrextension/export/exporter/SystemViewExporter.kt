package com.github.chriskn.structurizrextension.export.exporter

import com.github.chriskn.structurizrextension.export.createC4Diagram
import com.github.chriskn.structurizrextension.export.writer.BoundaryWriter
import com.github.chriskn.structurizrextension.export.writer.ElementWriter
import com.github.chriskn.structurizrextension.export.writer.FooterWriter
import com.github.chriskn.structurizrextension.export.writer.HeaderWriter
import com.github.chriskn.structurizrextension.export.writer.RelationshipWriter
import com.github.chriskn.structurizrextension.model.c4Location
import com.structurizr.export.Diagram
import com.structurizr.export.IndentingWriter
import com.structurizr.model.Location
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.model.enterprise
import com.structurizr.view.ElementView
import com.structurizr.view.ModelView
import com.structurizr.view.StaticView
import com.structurizr.view.SystemContextView
import com.structurizr.view.SystemLandscapeView
import com.structurizr.view.showEnterpriseBoundary

class SystemViewExporter(
    private val boundaryWriter: BoundaryWriter,
    private val footerWriter: FooterWriter,
    private val headerWriter: HeaderWriter,
    private val elementWriter: ElementWriter,
    private val relationshipWriter: RelationshipWriter
) {

    internal fun exportLandscapeView(view: SystemLandscapeView): Diagram {
        val writer = IndentingWriter()
        headerWriter.writeHeader(view, writer)

        val elementsInView = view.elements.sortedBy { it.id }

        val systemsAndPersonsInEnterprise = if (view.showEnterpriseBoundary) {
            writeElementsInEnterprise(view, writer)
        } else {
            emptyList()
        }

        writeElementsOutsideEnterprise(
            elementsOutsideEnterprise = elementsInView - systemsAndPersonsInEnterprise.toSet(),
            view,
            writer
        )

        relationshipWriter.writeRelationships(view, writer)
        footerWriter.writeFooter(view, writer)
        return createC4Diagram(view, writer.toString())
    }

    internal fun exportContextView(view: SystemContextView): Diagram {
        val writer = IndentingWriter()
        headerWriter.writeHeader(view, writer)

        val elementsInView = view.elements.sortedBy { it.id }

        val systemsAndPersonsInEnterprise = if (view.showEnterpriseBoundary) {
            writeElementsInEnterprise(view, writer)
        } else {
            emptyList()
        }

        writeElementsOutsideEnterprise(
            elementsOutsideEnterprise = elementsInView - systemsAndPersonsInEnterprise.toSet(),
            view,
            writer
        )

        relationshipWriter.writeRelationships(view, writer)
        footerWriter.writeFooter(view, writer)

        return createC4Diagram(view, writer.toString())
    }

    private fun writeElementsOutsideEnterprise(
        elementsOutsideEnterprise: List<ElementView>,
        view: StaticView,
        writer: IndentingWriter
    ) {
        for (elementView in elementsOutsideEnterprise) {
            elementWriter.writeElement(view, elementView.element, writer)
        }
        if (elementsOutsideEnterprise.isNotEmpty()) {
            writer.writeLine()
        }
    }

    private fun writeElementsInEnterprise(
        view: StaticView,
        writer: IndentingWriter
    ): List<ElementView> {
        val systemsAndPersonsInEnterprise = systemsInEnterprise(view)
        val enterpriseName = view.model.enterprise().name
        boundaryWriter.startEnterpriseBoundary(enterpriseName, writer, view)
        for (elementView in systemsAndPersonsInEnterprise) {
            elementWriter.writeElement(view, elementView.element, writer)
        }
        boundaryWriter.endEnterpriseBoundary(writer, view)
        return systemsAndPersonsInEnterprise
    }

    private fun systemsInEnterprise(view: ModelView): List<ElementView> =
        view.elements
            .filter {
                val element = it.element
                (element is SoftwareSystem && element.c4Location == Location.Internal) ||
                    (element is Person && element.c4Location == Location.Internal)
            }
            .toSet()
            .sortedBy { it.element.id }
}
