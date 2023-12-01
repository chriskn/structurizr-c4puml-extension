package com.github.chriskn.structurizrextension.view

import com.structurizr.model.StaticStructureElement
import com.structurizr.view.DynamicView
import com.structurizr.view.RelationshipView
import com.structurizr.view.publicOrder

private const val NUMBER_OF_ENDED_PARALLEL_FLOWS = "NUMBER_OF_PARALLEL_ROOT_SEQUENCES"
private const val AS_SEQUENCE_DIAGRAM = "plantuml.sequenceDiagram"

/**
 * Adds a dependency to the [NestedParallelSequenceContext] from [source] to [destination] with the given properties
 * All dependency's of a [DynamicView] will be resolved based on their properties against the existing dependency's
 * in the static model. Therefore, dependencies with the same source, target and technology must exist in the
 * static structure of the Structurizr model.
 *
 * @param source            the source of the dependency
 * @param destination       the destination of the dependency
 * @param description       the description of the dependency
 * @param technology        the technology of the dependency. Taken from the static structure dependency by default.
 *                          There must exist a static structure dependency with this technology.
 */
fun DynamicView.add(
    source: StaticStructureElement,
    destination: StaticStructureElement,
    description: String,
    technology: String? = null,
): RelationshipView {
    val relationshipView = this.add(source, description, technology, destination)
    relationshipView.publicOrder = (relationshipView.order.toInt() + this.numberOfEndedParallelFlows).toString()
    return relationshipView
}

/**
 * Enables support for parallel sequences with nested step ordering and step numbers
 * as alternative to the default parallel sequences.
 */
fun DynamicView.startNestedParallelSequence(): NestedParallelSequenceContext {
    val startOrder = if (this.relationships.isEmpty()) {
        1
    } else {
        this.relationships.last().order.toInt() + 1
    }
    return NestedParallelSequenceContext(this, startOrder.toString())
}

var DynamicView.numberOfEndedParallelFlows: Int
    get() {
        return this.properties.getOrDefault(NUMBER_OF_ENDED_PARALLEL_FLOWS, "0").toInt()
    }
    set(value) {
        this.addProperty(NUMBER_OF_ENDED_PARALLEL_FLOWS, value.toString())
    }

var DynamicView.renderAsSequenceDiagram: Boolean
    get() {
        return this.properties.getOrDefault(AS_SEQUENCE_DIAGRAM, "false").toBoolean()
    }
    set(value) {
        this.addProperty(AS_SEQUENCE_DIAGRAM, value.toString())
    }
