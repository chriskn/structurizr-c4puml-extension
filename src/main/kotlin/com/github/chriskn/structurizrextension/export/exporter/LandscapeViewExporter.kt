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
import com.structurizr.model.Element
import com.structurizr.model.Location
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ModelView
import com.structurizr.view.SystemLandscapeView
import com.structurizr.view.showEnterpriseBoundary

class LandscapeViewExporter(
    private val boundaryWriter: BoundaryWriter,
    private val footerWriter: FooterWriter,
    private val headerWriter: HeaderWriter,
    private val elementWriter: ElementWriter,
    private val relationshipWriter: RelationshipWriter
) {

    internal fun exportLandscapeView(view: SystemLandscapeView): Diagram {
        val writer = IndentingWriter()
        headerWriter.writeHeader(view, writer)

        val sortedElements = view.elements.sortedBy { it.id }
        val systemsInEnterprise = systemsInEnterprise(view)
        for (systemInEnterprise in systemsInEnterprise) {
            val showEnterpriseBoundary = view.showEnterpriseBoundary
            if (showEnterpriseBoundary) {
                val enterpriseName = view.elements.map { it.element }.first().model.enterprise.name
                boundaryWriter.startEnterpriseBoundary(enterpriseName, writer, view)
            }
            for (elementView in sortedElements) {
                if (elementView.element === systemInEnterprise) {
                    elementWriter.writeElement(view, elementView.element, writer)
                }
            }
            if (showEnterpriseBoundary) {
                boundaryWriter.endEnterpriseBoundary(writer, view)
            } else {
                writer.writeLine()
            }
        }

        var elementsWritten = false

        for (elementView in sortedElements) {
            elementWriter.writeElement(view, elementView.element, writer)
            elementsWritten = true
        }
        if (elementsWritten) {
            writer.writeLine()
        }

        relationshipWriter.writeRelationships(view, writer)

        footerWriter.writeFooter(view, writer)

        return createC4Diagram(view, writer.toString())
    }

    private fun systemsInEnterprise(view: ModelView): List<Element> =
        view.elements
            .map { it.element }
            .filter {
                (it is SoftwareSystem && it.c4Location == Location.Internal) || (it is Person && it.c4Location == Location.Internal)
            }
            .toSet()
            .sortedBy { it.id }
}
