package com.github.chriskn.structurizrextension.internal.export.view

import com.github.chriskn.structurizrextension.api.view.showExternalBoundaries
import com.github.chriskn.structurizrextension.internal.export.idOf
import com.structurizr.view.DynamicView

internal fun DynamicView.getBoundaryElements() =
    if (this.showExternalBoundaries) {
        this.elements
            .asSequence()
            .filter { it.element.parent != null }
            .map { it.element.parent }
            .toSet()
            .sortedBy { idOf(it) }
    } else {
        emptyList()
    }
