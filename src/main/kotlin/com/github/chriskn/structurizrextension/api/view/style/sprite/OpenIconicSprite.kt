package com.github.chriskn.structurizrextension.api.view.style.sprite

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.chriskn.structurizrextension.internal.view.style.toValidColor

/**
 * Open iconic sprite
 *
 * Can be used to include open iconic icons.
 * See [Open Iconic](https://github.com/iconic/open-iconic)
 *
 * @property name   the name of the icon
 * @property color  the color of the icon. Must be a valid hex code or a named color (e.g. "green")
 * @property scale  the scale of the icon
 * @constructor Create Open iconic sprite
 */
data class OpenIconicSprite(
    val name: String,
    val color: String? = null,
    val scale: Double? = null,
) : Sprite(scale) {

    @get:JsonIgnore
    internal val colorValidated: String? = color?.let { toValidColor(color) }
}
