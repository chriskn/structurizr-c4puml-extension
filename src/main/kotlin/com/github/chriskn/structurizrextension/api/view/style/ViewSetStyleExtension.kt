package com.github.chriskn.structurizrextension.api.view.style

import com.structurizr.view.ElementStyle
import com.structurizr.view.ViewSet

fun ViewSet.addElementStyle(elementStyle: ElementStyle) {
    this.configuration.styles.add(elementStyle)
}
