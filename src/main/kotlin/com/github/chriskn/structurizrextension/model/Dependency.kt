package com.github.chriskn.structurizrextension.model

import com.structurizr.model.Element
import com.structurizr.model.InteractionStyle

data class Dependency<out T : Element>(
    val target: T,
    val description: String,
    val technology: String? = null,
    val interactionStyle: InteractionStyle? = null
)
