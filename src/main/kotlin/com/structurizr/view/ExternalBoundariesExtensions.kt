package com.structurizr.view

@Suppress("DEPRECATION")
var DynamicView.showExternalBoundaries: Boolean
    get() = this.externalBoundariesVisible
    set(value) {
        this.externalBoundariesVisible = value
    }

@Suppress("DEPRECATION")
var ContainerView.showExternalSoftwareSystemBoundaries: Boolean
    get() = this.externalSoftwareSystemBoundariesVisible
    set(value) {
        this.externalSoftwareSystemBoundariesVisible = value
    }

@Suppress("DEPRECATION")
var ComponentView.showExternalContainerBoundaries: Boolean
    get() = this.externalContainerBoundariesVisible
    set(value) {
        this.setExternalSoftwareSystemBoundariesVisible(value)
    }

@Suppress("DEPRECATION")
var SystemLandscapeView.showEnterpriseBoundary: Boolean
    get() = this.isEnterpriseBoundaryVisible
    set(value) {
        this.isEnterpriseBoundaryVisible = value
    }
