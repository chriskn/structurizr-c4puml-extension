package com.github.chriskn.structurizrextension.api.view.style.sprite

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.chriskn.structurizrextension.api.view.style.toValidColor

data class OpenIconicSprite(
    val name: String,
    val color: String? = null,
    val scale: Double? = null,
) : Sprite(scale) {

    @get:JsonIgnore
    internal val validatedColor: String? = color?.let { toValidColor(color) }
}
