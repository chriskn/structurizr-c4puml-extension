package com.github.chriskn.structurizrextension.api.view.style.styles

import com.github.chriskn.structurizrextension.api.view.sprite.Sprite
import com.github.chriskn.structurizrextension.api.view.style.C4PUmlElementShape

/**
 * Element style.
 * Used to update the style of elements based on their tags.
 *
 * @property tag                the identifier of the style. Add the same tag to an element to apply the style
 * @property backgroundColor    the background color of the element. Must be a valid hex code or a named color (e.g. "green")
 * @property fontColor          the font color of the element. Must be a valid hex code or a named color (e.g. "green")
 * @property borderStyle        the border style of the element. See [C4PUmlLineStyle]
 * @property borderWidth        the width of the border. 1 is the default border width
 * @property borderColor        the border color for the element. Must be a valid hex code or a named color (e.g. "green")
 * @property shadowing          the element is rendered with a shadow if set to true
 * @property c4Shape            the shape of the element. See [C4PUmlElementShape]
 * @property sprite             the icon for the element
 * @property legendText         the legend text for the element
 * @property legendSprite       the legend icon for the element
 * @property technology         the technology of the element
 * @constructor Create new Element style
 */
data class ElementStyle(
    override val tag: String,
    override val backgroundColor: String? = null,
    override val fontColor: String? = null,
    override val borderStyle: C4PUmlLineStyle? = null,
    override val borderWidth: Int? = null,
    override val borderColor: String? = null,
    override val shadowing: Boolean = false,
    override val c4Shape: C4PUmlElementShape? = null,
    override val sprite: Sprite? = null,
    override val legendText: String? = null,
    override val legendSprite: Sprite? = null,
    val technology: String? = null,
) : ModelElementStyle() {

    init {
        validate()
    }
}
