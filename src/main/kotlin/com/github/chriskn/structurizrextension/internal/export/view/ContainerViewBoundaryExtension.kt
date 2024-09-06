package com.github.chriskn.structurizrextension.internal.export.view

import com.github.chriskn.structurizrextension.api.view.showExternalSoftwareSystemBoundaries
import com.github.chriskn.structurizrextension.internal.export.idOf
import com.structurizr.model.Container
import com.structurizr.view.ContainerView

internal fun ContainerView.getBoundarySystems() =
    if (this.showExternalSoftwareSystemBoundaries) {
        this.elements
            .asSequence()
            .map { it.element }
            .filterIsInstance<Container>()
            .map { it.softwareSystem }
            .toSet()
            .sortedBy { idOf(it) }
    } else {
        emptyList()
    }
