package com.github.chriskn.structurizrextension.api.view.style

import com.structurizr.view.ElementStyle
import com.structurizr.view.ViewSet

fun ViewSet.addElementStyle(elementStyle: ElementStyle) {
    this.configuration.styles.add(elementStyle)
}

fun ViewSet.getElementStyles(): List<ElementStyle> =
    this.configuration.styles.elements.toList()

internal fun ViewSet.clearElementStyles() {
    this.configuration.styles.elements.clear()
}
