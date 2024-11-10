package com.github.chriskn.structurizrextension.api.view.style.styles

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.chriskn.structurizrextension.api.view.sprite.Sprite
import com.github.chriskn.structurizrextension.internal.view.style.toValidColor

/**
 * Dependency style
 * Used to update the style of dependencies based on their tags.
 *
 * @property tag            the identifier of the style. Add the same tag to a dependency to apply the style
 * @property fontColor      the color of the font. Must be a valid hex code or a named color (e.g. "green")
 * @property sprite         the icon for the dependency
 * @property legendText     the legend text for the dependency
 * @property legendSprite   the legend icon for the dependency
 * @property technology     the technology for the dependency
 * @property lineColor      the line color for the dependency. Must be a valid hex code or a named color (e.g. "green")
 * @property lineStyle      the line style of the dependency. See [C4PUmlLineStyle]
 * @property lineWidth      the width of the line. 1 is the default line width
 * @constructor Create new Dependency style
 */
data class DependencyStyle(
    val tag: String,
    val fontColor: String? = null,
    val sprite: Sprite? = null,
    val legendText: String? = null,
    val legendSprite: Sprite? = null,
    val technology: String? = null,
    val lineColor: String? = null,
    val lineStyle: C4PUmlLineStyle? = null,
    val lineWidth: Int? = null,
) {

    @get:JsonIgnore
    internal val fontColorValidated = toValidColor(fontColor)

    @get:JsonIgnore
    internal val lineColorValidated = toValidColor(lineColor)

    init {
        require(tag.isNotBlank()) { "tag must not be blank" }
    }
}
