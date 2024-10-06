package com.github.chriskn.structurizrextension.api.view.style

import com.github.chriskn.structurizrextension.api.view.style.styles.BoundaryStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.ElementStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.PersonStyle
import com.github.chriskn.structurizrextension.internal.export.view.style.boundaryStyleFromJson
import com.github.chriskn.structurizrextension.internal.export.view.style.elementStyleFromJson
import com.github.chriskn.structurizrextension.internal.export.view.style.personStyleFromJson
import com.github.chriskn.structurizrextension.internal.export.view.style.toJson
import com.structurizr.view.View

internal const val ELEMENT_STYLE_PROPERTY_NAME_PREFIX = "c4:elementStyle"
internal const val BOUNDARY_STYLE_PROPERTY_NAME_PREFIX = "c4:boundaryStyle"
internal const val PERSON_STYLE_PROPERTY_NAME_PREFIX = "c4:personStyle"

fun View.addElementStyle(elementStyle: ElementStyle) {
    this.addProperty("$ELEMENT_STYLE_PROPERTY_NAME_PREFIX:${elementStyle.tag}", elementStyle.toJson())
}

fun View.getElementStyles(): List<ElementStyle> =
    this.properties
        .filter { it.key.startsWith(ELEMENT_STYLE_PROPERTY_NAME_PREFIX) }
        .map { elementStyleFromJson(it.value) }

fun View.addBoundaryStyle(boundaryStyle: BoundaryStyle) {
    this.addProperty("$BOUNDARY_STYLE_PROPERTY_NAME_PREFIX:${boundaryStyle.tag}", boundaryStyle.toJson())
}

fun View.getBoundaryStyles(): List<BoundaryStyle> =
    this.properties
        .filter { it.key.startsWith(BOUNDARY_STYLE_PROPERTY_NAME_PREFIX) }
        .map { boundaryStyleFromJson(it.value) }

fun View.addPersonStyle(personStyle: PersonStyle) {
    this.addProperty("$PERSON_STYLE_PROPERTY_NAME_PREFIX:${personStyle.tag}", personStyle.toJson())
}

fun View.getPersonStyles(): List<PersonStyle> =
    this.properties
        .filter { it.key.startsWith(PERSON_STYLE_PROPERTY_NAME_PREFIX) }
        .map { personStyleFromJson(it.value) }
