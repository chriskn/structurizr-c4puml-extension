package com.github.chriskn.structurizrextension.api.view

import com.structurizr.view.ComponentView
import com.structurizr.view.ContainerView
import com.structurizr.view.DynamicView
import com.structurizr.view.SystemContextView
import com.structurizr.view.SystemLandscapeView

private const val SHOW_BOUNDARY_PROPERTY = "SHOW_BOUNDARY"

/**
 * If set to true, higher model elements (systems or containers) will be shown as boundaries. default false
 */
var DynamicView.showExternalBoundaries: Boolean
    get() = this.properties[SHOW_BOUNDARY_PROPERTY].toBoolean()
    set(value) {
        this.addProperty(SHOW_BOUNDARY_PROPERTY, value.toString())
    }

/**
 * If set to true, software systems will be shown as boundaries. default false
 */
var ContainerView.showExternalSoftwareSystemBoundaries: Boolean
    get() = this.properties[SHOW_BOUNDARY_PROPERTY].toBoolean()
    set(value) {
        this.addProperty(SHOW_BOUNDARY_PROPERTY, value.toString())
    }

/**
 * If set to true, container will be shown as boundaries. default false
 */
var ComponentView.showExternalContainerBoundaries: Boolean
    get() = this.properties[SHOW_BOUNDARY_PROPERTY].toBoolean()
    set(value) {
        this.addProperty(SHOW_BOUNDARY_PROPERTY, value.toString())
    }

/**
 * If set to true, enterprise boundary will be shown. default false
 */
var SystemLandscapeView.showEnterpriseBoundary: Boolean
    get() = this.properties[SHOW_BOUNDARY_PROPERTY].toBoolean()
    set(value) {
        this.addProperty(SHOW_BOUNDARY_PROPERTY, value.toString())
    }

/**
 * If set to true, enterprise boundary will be shown. default false
 */
var SystemContextView.showEnterpriseBoundary: Boolean
    get() = this.properties[SHOW_BOUNDARY_PROPERTY].toBoolean()
    set(value) {
        this.addProperty(SHOW_BOUNDARY_PROPERTY, value.toString())
    }
