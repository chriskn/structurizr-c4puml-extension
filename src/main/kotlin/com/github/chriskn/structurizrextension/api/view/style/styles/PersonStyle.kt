package com.github.chriskn.structurizrextension.api.view.style.styles

import com.github.chriskn.structurizrextension.api.view.sprite.Sprite
import com.github.chriskn.structurizrextension.api.view.style.C4PUmlElementShape
import com.github.chriskn.structurizrextension.internal.view.style.toValidColor

/**
 * Person style.
 * Used to update the style of persons based on their tags.
 *
 * @property tag                the identifier of the style. Add the same tag to an person to apply the style
 * @property backgroundColor    the background color of the person. Must be a valid hex code or a named color (e.g. "green")
 * @property fontColor          the font color of the person. Must be a valid hex code or a named color (e.g. "green")
 * @property borderStyle        the border style of the person. See [C4PUmlLineStyle]
 * @property borderWidth        the width of the border. 1 is the default border width
 * @property borderColor        the border color for the person. Must be a valid hex code or a named color (e.g. "green")
 * @property shadowing          the person is rendered with a shadow if set to true
 * @property c4Shape            the shape of the person. See [C4PUmlElementShape]
 * @property sprite             the icon for the person
 * @property legendText         the legend text for the person
 * @property legendSprite       the legend icon for the person
 * @constructor Create new Person style
 */
data class PersonStyle(
    override val tag: String,
    override val backgroundColor: String?,
    override val fontColor: String?,
    override val borderStyle: C4PUmlLineStyle?,
    override val borderWidth: Int?,
    override val borderColor: String?,
    override val shadowing: Boolean,
    override val c4Shape: C4PUmlElementShape?,
    override val sprite: Sprite?,
    override val legendText: String?,
    override val legendSprite: Sprite?,
) : ModelElementStyle() {

    init {
        validate()
    }
}

@Suppress("LongParameterList")
fun createPersonStyle(
    tag: String,
    backgroundColor: String? = null,
    fontColor: String? = null,
    borderStyle: C4PUmlLineStyle? = null,
    borderWidth: Int? = null,
    borderColor: String? = null,
    shadowing: Boolean = false,
    c4Shape: C4PUmlElementShape? = null,
    sprite: Sprite? = null,
    legendText: String? = null,
    legendSprite: Sprite? = null,
): PersonStyle {
    require(tag.isNotBlank()) { "tag must not be blank" }
    return PersonStyle(
        tag = tag,
        backgroundColor = toValidColor(backgroundColor),
        fontColor = toValidColor(fontColor),
        borderStyle = borderStyle,
        borderWidth = borderWidth,
        borderColor = toValidColor(borderColor),
        shadowing = shadowing,
        c4Shape = c4Shape,
        sprite = sprite,
        legendText = legendText,
        legendSprite = legendSprite,
    )
}
