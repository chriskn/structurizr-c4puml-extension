package com.github.chriskn.structurizrextension.api.view.sprite.sprites

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.chriskn.structurizrextension.internal.view.style.toValidColor

/**
 * Open iconic sprite
 *
 * Can be used to include open iconic icons.
 * See [Open Iconic](https://github.com/iconic/open-iconic)
 *
 * @property name   the name of the icon starting with '&'. See [Open Iconic sprites](https://plantuml.com/de/openiconic)
 * @property color  the color of the icon. Must be a valid hex code or a named color (e.g. "green")
 * @property scale  the scale of the icon
 *
 * @constructor Create Open iconic sprite
 */
data class OpenIconicSprite(
    override val name: String,
    val color: String? = null,
    val scale: Double? = null,
) : Sprite(name, scale) {

    @get:JsonIgnore
    internal val colorValidated: String? = color?.let { toValidColor(color) }

    init {
        require(
            name.startsWith("&") && name.length > 1
        ) { "Icon name must start with '&' followed by a name but was $name" }
    }
}
