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
import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.Element
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.DynamicView
import com.structurizr.view.ModelView

class DynamicViewExporter(
    private val boundaryWriter: BoundaryWriter,
    private val footerWriter: FooterWriter,
    private val headerWriter: HeaderWriter,
    private val elementWriter: ElementWriter,
    private val relationshipWriter: RelationshipWriter
) {
    internal fun exportDynamicView(view: DynamicView): Diagram {
        val writer = IndentingWriter()
        headerWriter.writeHeader(view, writer)
        val elements: MutableSet<Element> = LinkedHashSet()
        for (relationshipView in view.relationships) {
            elements.add(relationshipView.relationship.source)
            elements.add(relationshipView.relationship.destination)
        }
        if (view.externalBoundariesVisible) {
            writeChildrenInParentBoundaries(elements, view, writer)
            elements
                .filter { it.parent == null }
                .forEach { elementWriter.writeElement(view, it, writer) }
        } else {
            elements.forEach { elementWriter.writeElement(view, it, writer) }
        }
        relationshipWriter.writeRelationships(view, writer)
        footerWriter.writeFooter(view, writer)
        return createC4Diagram(view, writer.toString())
    }

    private fun writeChildrenInParentBoundaries(
        elements: Set<Element>,
        view: ModelView,
        writer: IndentingWriter
    ) {
        val hierarchy: Map<SoftwareSystem, Map<Container, List<Component>>> = buildElementHierarchy(elements)
        hierarchy.forEach { (system, containerToComponents) ->
            boundaryWriter.startSoftwareSystemBoundary(system, writer)
            containerToComponents.forEach { (container, components) ->
                if (components.isEmpty()) {
                    elementWriter.writeElement(view, container, writer)
                } else {
                    writeChildrenInParentBoundary(container, components, view, writer)
                }
            }
            boundaryWriter.endSoftwareSystemBoundary(writer)
        }
    }

    private fun writeChildrenInParentBoundary(
        parent: Element,
        children: List<Element>,
        view: ModelView,
        writer: IndentingWriter,
    ) {
        when (parent) {
            is SoftwareSystem -> boundaryWriter.startSoftwareSystemBoundary(parent, writer)
            is Container -> boundaryWriter.startContainerBoundary(parent, writer)
            else -> boundaryWriter.startGroupBoundary(idOf(parent), writer)
        }
        children.forEach { elementWriter.writeElement(view, it, writer) }
        when (parent) {
            is SoftwareSystem -> boundaryWriter.endSoftwareSystemBoundary(writer)
            is Container -> boundaryWriter.endContainerBoundary(writer)
            else -> boundaryWriter.endGroupBoundary(writer)
        }
    }

    private fun buildElementHierarchy(
        elements: Set<Element>
    ): Map<SoftwareSystem, Map<Container, List<Component>>> {
        val elementsByParent = elements.groupBy { it.parent }
        val containerGroups = elementsByParent.filterKeys { it is Container } as Map<Container, List<Component>>
        val systemGroups = elementsByParent.filterKeys { it is SoftwareSystem } as Map<SoftwareSystem, List<Container>>
        val hierarchy: MutableMap<SoftwareSystem, MutableMap<Container, List<Component>>> = mutableMapOf()
        systemGroups.forEach { (system, containers) ->
            hierarchy[system] = containers
                .associateWith { container -> containerGroups.getOrDefault(container, emptyList()) }
                .toMutableMap()
        }
        containerGroups.forEach { (container, components) ->
            val system = container.softwareSystem
            if (hierarchy.containsKey(system)) {
                hierarchy[system] = hierarchy[system]!!.plus(container to components).toMutableMap()
            } else {
                hierarchy[system] = mutableMapOf(container to components)
            }
        }
        return hierarchy
    }
}
