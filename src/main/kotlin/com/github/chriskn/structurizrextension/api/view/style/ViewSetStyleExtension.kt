package com.github.chriskn.structurizrextension.api.view.style

import com.github.chriskn.structurizrextension.api.view.style.styles.BoundaryStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.ElementStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.PersonStyle
import com.github.chriskn.structurizrextension.internal.export.view.style.boundaryStyleFromJson
import com.github.chriskn.structurizrextension.internal.export.view.style.elementStyleFromJson
import com.github.chriskn.structurizrextension.internal.export.view.style.personStyleFromJson
import com.github.chriskn.structurizrextension.internal.export.view.style.toJson
import com.structurizr.view.ViewSet
import com.structurizr.view.clearBoundaryStyles
import com.structurizr.view.clearElementStyles
import com.structurizr.view.clearPersonStyles

fun ViewSet.addElementStyle(elementStyle: ElementStyle) {
    this.configuration.addProperty("$ELEMENT_STYLE_PROPERTY_NAME_PREFIX:${elementStyle.tag}", elementStyle.toJson())
}

fun ViewSet.getElementStyles(): List<ElementStyle> =
    this.configuration.properties
        .filter { it.key.startsWith(ELEMENT_STYLE_PROPERTY_NAME_PREFIX) }
        .map { elementStyleFromJson(it.value) }

internal fun ViewSet.clearElementStyles() {
    this.configuration.clearElementStyles()
}

fun ViewSet.addBoundaryStyle(boundaryStyle: BoundaryStyle) {
    this.configuration.addProperty("$BOUNDARY_STYLE_PROPERTY_NAME_PREFIX:${boundaryStyle.tag}", boundaryStyle.toJson())
}

fun ViewSet.getBoundaryStyles(): List<BoundaryStyle> =
    this.configuration.properties
        .filter { it.key.startsWith(BOUNDARY_STYLE_PROPERTY_NAME_PREFIX) }
        .map { boundaryStyleFromJson(it.value) }

internal fun ViewSet.clearBoundaryStyles() {
    this.configuration.clearBoundaryStyles()
}

fun ViewSet.addPersonStyle(personStyle: PersonStyle) {
    this.configuration.addProperty("$PERSON_STYLE_PROPERTY_NAME_PREFIX:${personStyle.tag}", personStyle.toJson())
}

fun ViewSet.getPersonStyles(): List<PersonStyle> =
    this.configuration.properties
        .filter { it.key.startsWith(PERSON_STYLE_PROPERTY_NAME_PREFIX) }
        .map { personStyleFromJson(it.value) }

internal fun ViewSet.clearPersonStyles() {
    this.configuration.clearPersonStyles()
}
