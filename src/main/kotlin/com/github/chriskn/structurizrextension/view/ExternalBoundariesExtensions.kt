package com.github.chriskn.structurizrextension.view

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
