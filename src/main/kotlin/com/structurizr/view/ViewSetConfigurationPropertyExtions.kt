package com.structurizr.view

import com.github.chriskn.structurizrextension.api.view.style.BOUNDARY_STYLE_PROPERTY_NAME_PREFIX
import com.github.chriskn.structurizrextension.api.view.style.ELEMENT_STYLE_PROPERTY_NAME_PREFIX
import com.github.chriskn.structurizrextension.api.view.style.PERSON_STYLE_PROPERTY_NAME_PREFIX

fun Configuration.clearBoundaryStyles() {
    this.properties = this
        .properties
        .filterKeys { !it.startsWith(BOUNDARY_STYLE_PROPERTY_NAME_PREFIX) }
}

fun Configuration.clearPersonStyles() {
    this.properties = this
        .properties
        .filterKeys { !it.startsWith(PERSON_STYLE_PROPERTY_NAME_PREFIX) }
}

fun Configuration.clearElementStyles() {
    this.properties = this
        .properties
        .filterKeys { !it.startsWith(ELEMENT_STYLE_PROPERTY_NAME_PREFIX) }
}
