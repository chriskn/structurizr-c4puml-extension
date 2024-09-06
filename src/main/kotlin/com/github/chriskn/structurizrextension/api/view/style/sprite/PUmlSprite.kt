package com.github.chriskn.structurizrextension.api.view.style.sprite

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.internal.view.style.toValidColor
import java.net.MalformedURLException

/**
 * Plant UML sprite
 *
 * Can be used to include plantuml icons.
 *
 * @property name   the name of the icon
 * @property url    url pointing to a .puml file
 * @property color  the color of the icon. Must be a valid hex code or a named color (e.g. "green")
 * @property scale  the scale of the icon
 * @constructor Create Plant UML sprite
 */
data class PUmlSprite(
    val name: String,
    /**
     * Url used for include statement
     *
     * @throws IllegalArgumentException if url does not point to puml file
     * @throws MalformedURLException if url is invalid
     */
    val url: String,
    val color: String? = null,
    val scale: Double? = null,
) : Sprite(scale) {

    @get:JsonIgnore
    internal val colorValidated: String? = color?.let { toValidColor(color) }

    init {
        IconRegistry.validate(name, url)
    }
}
