package com.github.chriskn.structurizrextension.model

import com.structurizr.model.Element
import com.structurizr.model.InteractionStyle

data class Dependency(
    val target: Element,
    val description: String,
    val technology: String? = null,
    val interactionStyle: InteractionStyle? = null
)
