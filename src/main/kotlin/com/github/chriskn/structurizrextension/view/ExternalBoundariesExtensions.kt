package com.github.chriskn.structurizrextension.view

import com.structurizr.view.ComponentView
import com.structurizr.view.ContainerView
import com.structurizr.view.DynamicView

var DynamicView.showExternalBoundaries: Boolean
    get() = this.externalBoundariesVisible
    set(value) {
        @Suppress("DEPRECATION")
        this.externalBoundariesVisible = value
    }

var ContainerView.showExternalSoftwareSystemBoundaries: Boolean
    get() = this.externalSoftwareSystemBoundariesVisible
    set(value) {
        @Suppress("DEPRECATION")
        this.externalSoftwareSystemBoundariesVisible = value
    }

var ComponentView.showExternalContainerBoundaries: Boolean
    // naming bug in structurizr
    get() = this.externalContainerBoundariesVisible
    set(value) {
        @Suppress("DEPRECATION")
        this.setExternalSoftwareSystemBoundariesVisible(value)
    }
