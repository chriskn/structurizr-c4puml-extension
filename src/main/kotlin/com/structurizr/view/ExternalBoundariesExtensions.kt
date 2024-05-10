package com.structurizr.view

var DynamicView.showExternalBoundaries: Boolean
    @Suppress("Deprecated")
    get() = this.externalBoundariesVisible
    set(value) {
        this.externalBoundariesVisible = value
    }

var ContainerView.showExternalSoftwareSystemBoundaries: Boolean
    @Suppress("Deprecated")
    get() = this.externalSoftwareSystemBoundariesVisible
    set(value) {
        this.externalSoftwareSystemBoundariesVisible = value
    }

var ComponentView.showExternalContainerBoundaries: Boolean
    @Suppress("Deprecated")
    get() = this.externalContainerBoundariesVisible
    set(value) {
        this.setExternalSoftwareSystemBoundariesVisible(value)
    }

var SystemLandscapeView.showEnterpriseBoundary: Boolean
    @Suppress("Deprecated")
    get() = this.isEnterpriseBoundaryVisible
    set(value) {
        this.setEnterpriseBoundaryVisible(value)
    }
