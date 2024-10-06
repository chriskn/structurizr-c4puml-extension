package com.github.chriskn.structurizrextension.internal.export.view

import com.github.chriskn.structurizrextension.api.view.showExternalContainerBoundaries
import com.github.chriskn.structurizrextension.internal.export.idOf
import com.structurizr.model.Component
import com.structurizr.view.ComponentView
import com.structurizr.view.ElementView

internal fun ComponentView.getBoundaryContainer() =
    if (this.showExternalContainerBoundaries) {
        this.elements
            .asSequence()
            .map { obj: ElementView -> obj.element }
            .filterIsInstance<Component>()
            .map { it.container }
            .toSet()
            .sortedBy { idOf(it) }
    } else {
        emptyList()
    }
