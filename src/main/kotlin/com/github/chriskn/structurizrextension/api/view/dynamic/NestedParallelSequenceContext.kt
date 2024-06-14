package com.github.chriskn.structurizrextension.api.view.dynamic

import com.structurizr.model.StaticStructureElement
import com.structurizr.view.DynamicView
import com.structurizr.view.RelationshipView
import com.structurizr.view.orderInternal

/**
 * Wrapper around [DynamicView] to manage nested numbering for parallel sequences.
 */
data class NestedParallelSequenceContext(
    private val dynamicView: DynamicView,
    private val startOrder: String,
    private val isRoot: Boolean = true
) {

    private var parallelCounter = 0
    private var latestOrder = startOrder

    init {
        dynamicView.startParallelSequence()
    }

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
    fun add(
        source: StaticStructureElement,
        destination: StaticStructureElement,
        description: String,
        technology: String? = null,
    ): RelationshipView {
        val view = dynamicView.add(
            source = source,
            destination = destination,
            description = description,
            technology = technology
        )
        view.orderInternal = getNextOrder()

        return view
    }

    private fun getNextOrder(): String {
        latestOrder = "$startOrder.${++parallelCounter}"
        return latestOrder
    }

    fun startNestedParallelSequence(): NestedParallelSequenceContext =
        NestedParallelSequenceContext(dynamicView, latestOrder, false)

    fun endParallelSequence() {
        if (isRoot) {
            dynamicView.numberOfEndedParallelFlows += 1
        }
        dynamicView.endParallelSequence()
    }
}
