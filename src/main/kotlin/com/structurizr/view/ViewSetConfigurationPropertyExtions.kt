package com.structurizr.view

import com.github.chriskn.structurizrextension.internal.export.view.style.BOUNDARY_STYLE_PROPERTY_NAME_PREFIX
import com.github.chriskn.structurizrextension.internal.export.view.style.DEPENDENCY_STYLE_PROPERTY_NAME_PREFIX
import com.github.chriskn.structurizrextension.internal.export.view.style.ELEMENT_STYLE_PROPERTY_NAME_PREFIX
import com.github.chriskn.structurizrextension.internal.export.view.style.PERSON_STYLE_PROPERTY_NAME_PREFIX

internal fun Configuration.clearBoundaryStyles() {
    this.properties = this.properties
        .filterKeys { !it.startsWith(BOUNDARY_STYLE_PROPERTY_NAME_PREFIX) }
}

internal fun Configuration.clearElementStyles() {
    this.properties = this.properties
        .filterKeys { !it.startsWith(ELEMENT_STYLE_PROPERTY_NAME_PREFIX) }
}

internal fun Configuration.clearPersonStyles() {
    this.properties = this.properties
        .filterKeys { !it.startsWith(PERSON_STYLE_PROPERTY_NAME_PREFIX) }
}

internal fun Configuration.clearDependencyStyles() {
    this.properties = this.properties
        .filterKeys { !it.startsWith(DEPENDENCY_STYLE_PROPERTY_NAME_PREFIX) }
}
