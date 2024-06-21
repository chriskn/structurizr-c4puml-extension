package com.github.chriskn.structurizrextension.internal.export.view

import com.github.chriskn.structurizrextension.api.model.c4Location
import com.github.chriskn.structurizrextension.api.model.enterpriseName
import com.github.chriskn.structurizrextension.api.view.showEnterpriseBoundary
import com.github.chriskn.structurizrextension.internal.export.ExtendedC4PlantUMLExporter
import com.github.chriskn.structurizrextension.internal.export.idOf
import com.structurizr.export.Diagram
import com.structurizr.export.IndentingWriter
import com.structurizr.model.Location
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ElementView
import com.structurizr.view.ModelView
import com.structurizr.view.StaticView
import com.structurizr.view.SystemContextView
import com.structurizr.view.SystemLandscapeView

internal class SystemViewExporter(
    private val c4PlantUMLExporter: ExtendedC4PlantUMLExporter
) {

    fun exportLandscapeView(view: SystemLandscapeView): Diagram {
        val writer = IndentingWriter()
        c4PlantUMLExporter.writeHeader(view, writer)

        val elementsInView = view.elements.sortedBy { idOf(it.element) }

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

        c4PlantUMLExporter.writeRelationships(view, writer)
        c4PlantUMLExporter.writeFooter(view, writer)
        return c4PlantUMLExporter.createDiagram(view, writer.toString())
    }

    internal fun exportContextView(view: SystemContextView): Diagram {
        val writer = IndentingWriter()
        c4PlantUMLExporter.writeHeader(view, writer)

        val elementsInView = view.elements.sortedBy { idOf(it.element) }

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

        c4PlantUMLExporter.writeRelationships(view, writer)
        c4PlantUMLExporter.writeFooter(view, writer)

        return c4PlantUMLExporter.createDiagram(view, writer.toString())
    }

    private fun writeElementsOutsideEnterprise(
        elementsOutsideEnterprise: List<ElementView>,
        view: StaticView,
        writer: IndentingWriter
    ) {
        for (elementView in elementsOutsideEnterprise) {
            c4PlantUMLExporter.writeElement(view, elementView.element, writer)
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
        val enterpriseName = view.model.enterpriseName ?: ""
        c4PlantUMLExporter.startEnterpriseBoundary(view, enterpriseName, writer)
        for (elementView in systemsAndPersonsInEnterprise) {
            c4PlantUMLExporter.writeElement(view, elementView.element, writer)
        }
        c4PlantUMLExporter.endEnterpriseBoundary(view, writer)
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
            .sortedBy { idOf(it.element) }
}
