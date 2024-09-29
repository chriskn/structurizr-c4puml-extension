package com.github.chriskn.structurizrextension.api.view.style

import com.github.chriskn.structurizrextension.internal.export.view.style.elementStyleFromJson
import com.github.chriskn.structurizrextension.internal.export.view.style.toJson
import com.structurizr.view.ElementStyle
import com.structurizr.view.View

private const val ELEMENT_STYLE_PROPERTY_NAME_PREFIX = "c4:elementStyle"

fun View.addElementStyle(elementStyle: ElementStyle) {
    this.addProperty("$ELEMENT_STYLE_PROPERTY_NAME_PREFIX:${elementStyle.tag}", elementStyle.toJson())
}

fun View.getElementStyles(): List<ElementStyle> =
    this.properties
        .filterKeys { it.startsWith(ELEMENT_STYLE_PROPERTY_NAME_PREFIX) }
        .values
        .map { elementStyleFromJson(it) }
