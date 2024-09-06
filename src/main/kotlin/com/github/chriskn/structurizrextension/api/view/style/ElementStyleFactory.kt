package com.github.chriskn.structurizrextension.api.view.style

import com.structurizr.view.Border
import com.structurizr.view.Color
import com.structurizr.view.ElementStyle
import com.structurizr.view.createElementStyleFromTag

internal const val BORDER_COLOR_PROPERTY_NAME = "c4:borderColor"
internal const val SHADOWING_PROPERTY_NAME = "c4:shadowing"
internal const val SHAPE_PROPERTY_NAME = "c4:shape"
internal const val SPRITE_PROPERTY_NAME = "c4:sprite"
internal const val LEGEND_SPRITE_PROPERTY_NAME = "c4:legendSprite"
internal const val LEGEND_TEXT_PROPERTY_NAME = "c4:legendText"
internal const val TECHNOLOGY_PROPERTY_NAME = "c4:technology"

@Suppress("LongParameterList")
fun createElementStyle(
    tag: String,
    background: String? = null,
    fontColor: String? = null,
    fontSize: Int? = null,
    border: Border? = null,
    borderWith: Int? = null,
    borderColor: String? = null,
    technology: String? = null,
    shadowing: Boolean? = null,
    shape: Shape? = null,
    sprite: Sprite? = null,
    legendText: String? = null,
    legendSprite: Sprite? = null,
): ElementStyle {
    val style = createElementStyleFromTag(tag)
    background?.let { style.background = background }
    fontColor?.let { style.color = fontColor }
    fontSize?.let { style.fontSize = fontSize }
    border?.let { style.border = border }
    borderWith?.let { style.strokeWidth = borderWith }
    borderColor?.let { style.addProperty(BORDER_COLOR_PROPERTY_NAME, toValidColor(borderColor)) }
    technology?.let { style.addProperty(TECHNOLOGY_PROPERTY_NAME, technology) }
    shadowing?.let { style.addProperty(SHADOWING_PROPERTY_NAME, shadowing.toString()) }
    shape?.let { style.addProperty(SHAPE_PROPERTY_NAME, shape.toString()) }
    sprite?.let { style.addProperty(SPRITE_PROPERTY_NAME, sprite.toString()) }
    legendText?.let { style.addProperty(LEGEND_TEXT_PROPERTY_NAME, legendText) }
    legendSprite?.let { style.addProperty(LEGEND_SPRITE_PROPERTY_NAME, legendSprite.toString()) }
    return style
}

private fun toValidColor(color: String): String =
    if (Color.isHexColorCode(color)) {
        color.lowercase()
    } else {
        val hexColorCode = Color.fromColorNameToHexColorCode(color)
        requireNotNull(hexColorCode) { "$color is not a valid hex color code or HTML color name." }
        hexColorCode.lowercase()
    }
