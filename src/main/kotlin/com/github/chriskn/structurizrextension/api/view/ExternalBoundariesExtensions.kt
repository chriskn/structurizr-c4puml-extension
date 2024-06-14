package com.github.chriskn.structurizrextension.api.view

import com.structurizr.view.ComponentView
import com.structurizr.view.ContainerView
import com.structurizr.view.DynamicView
import com.structurizr.view.SystemContextView
import com.structurizr.view.SystemLandscapeView

private const val SHOW_BOUNDARY_PROPERTY = "SHOW_BOUNDARY"

var DynamicView.showExternalBoundaries: Boolean
    get() = this.properties[SHOW_BOUNDARY_PROPERTY].toBoolean()
    set(value) {
        this.addProperty(SHOW_BOUNDARY_PROPERTY, value.toString())
    }

var ContainerView.showExternalSoftwareSystemBoundaries: Boolean
    get() = this.properties[SHOW_BOUNDARY_PROPERTY].toBoolean()
    set(value) {
        this.addProperty(SHOW_BOUNDARY_PROPERTY, value.toString())
    }

var ComponentView.showExternalContainerBoundaries: Boolean
    get() = this.properties[SHOW_BOUNDARY_PROPERTY].toBoolean()
    set(value) {
        this.addProperty(SHOW_BOUNDARY_PROPERTY, value.toString())
    }

var SystemLandscapeView.showEnterpriseBoundary: Boolean
    get() = this.properties[SHOW_BOUNDARY_PROPERTY].toBoolean()
    set(value) {
        this.addProperty(SHOW_BOUNDARY_PROPERTY, value.toString())
    }

var SystemContextView.showEnterpriseBoundary: Boolean
    get() = this.properties[SHOW_BOUNDARY_PROPERTY].toBoolean()
    set(value) {
        this.addProperty(SHOW_BOUNDARY_PROPERTY, value.toString())
    }
