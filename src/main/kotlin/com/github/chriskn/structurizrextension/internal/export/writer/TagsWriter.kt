package com.github.chriskn.structurizrextension.internal.export.writer

import com.structurizr.model.Element
import com.structurizr.view.ModelView

internal fun tagsString(view: ModelView, element: Element):String {
    val tags = view.viewSet.configuration.styles.findElementStyle(element).tag.replaceFirst("Element,".toRegex(), "");
    return """, ${'$'}tags="$tags""""
}