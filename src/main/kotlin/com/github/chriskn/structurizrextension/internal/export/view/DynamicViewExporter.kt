package com.github.chriskn.structurizrextension.internal.export.view

import com.github.chriskn.structurizrextension.api.view.showExternalBoundaries
import com.github.chriskn.structurizrextension.internal.export.ExtendedC4PlantUMLExporter
import com.github.chriskn.structurizrextension.internal.export.idOf
import com.structurizr.export.Diagram
import com.structurizr.export.IndentingWriter
import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.Element
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.DynamicView
import com.structurizr.view.ModelView

internal class DynamicViewExporter(
    private val c4PlantUMLExporter: ExtendedC4PlantUMLExporter,
) {
    fun exportDynamicView(view: DynamicView): Diagram {
        val writer = IndentingWriter()
        c4PlantUMLExporter.writeHeader(view, writer)
        // collecting elements based on their relationships
        // is needed to ensure order and therefore correctness in sequence diagrams
        val elements = view.relationships
            .map { listOf(it.relationship.source, it.relationship.destination) }
            .flatten()
            .toSet()

        if (view.showExternalBoundaries) {
            writeChildrenInParentBoundaries(elements, view, writer)
            elements
                .filter { it.parent == null }
                .forEach { c4PlantUMLExporter.writeElement(view, it, writer) }
        } else {
            elements.forEach { c4PlantUMLExporter.writeElement(view, it, writer) }
        }
        c4PlantUMLExporter.writeRelationships(view, writer)
        c4PlantUMLExporter.writeFooter(view, writer)
        return c4PlantUMLExporter.createDiagram(view, writer.toString())
    }

    private fun writeChildrenInParentBoundaries(
        elements: Set<Element>,
        view: ModelView,
        writer: IndentingWriter
    ) {
        val hierarchy: Map<SoftwareSystem, Map<Container, List<Component>>> = buildElementHierarchy(elements)
        hierarchy.forEach { (system, containerToComponents) ->
            c4PlantUMLExporter.startSoftwareSystemBoundary(view, system, writer)
            containerToComponents.forEach { (container, components) ->
                if (components.isEmpty()) {
                    c4PlantUMLExporter.writeElement(view, container, writer)
                } else {
                    writeChildrenInParentBoundary(container, components, view, writer)
                }
            }
            c4PlantUMLExporter.endSoftwareSystemBoundary(view, writer)
        }
    }

    private fun writeChildrenInParentBoundary(
        parent: Element,
        children: List<Element>,
        view: ModelView,
        writer: IndentingWriter,
    ) {
        when (parent) {
            is SoftwareSystem -> c4PlantUMLExporter.startSoftwareSystemBoundary(view, parent, writer)
            is Container -> c4PlantUMLExporter.startContainerBoundary(view, parent, writer)
            else -> c4PlantUMLExporter.startGroupBoundary(view, idOf(parent), writer)
        }
        children.forEach { c4PlantUMLExporter.writeElement(view, it, writer) }
        when (parent) {
            is SoftwareSystem -> c4PlantUMLExporter.endSoftwareSystemBoundary(view, writer)
            is Container -> c4PlantUMLExporter.endContainerBoundary(view, writer)
            else -> c4PlantUMLExporter.endGroupBoundary(view, writer)
        }
    }

    @Suppress("UNCHECKED_CAST")
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
                hierarchy[system] = hierarchy.getValue(system).plus(container to components).toMutableMap()
            } else {
                hierarchy[system] = mutableMapOf(container to components)
            }
        }
        return hierarchy
    }
}
