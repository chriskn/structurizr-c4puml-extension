package com.github.chriskn.structurizrextension.api.view.style

import com.github.chriskn.structurizrextension.api.view.style.sprite.Sprite
import com.github.chriskn.structurizrextension.internal.export.view.style.spriteFormJson
import com.github.chriskn.structurizrextension.internal.export.view.style.toJson
import com.structurizr.view.Border
import com.structurizr.view.ElementStyle
import com.structurizr.view.createElementStyleFromTag

private const val BORDER_COLOR_PROPERTY_NAME = "c4:borderColor"
private const val SHADOWING_PROPERTY_NAME = "c4:shadowing"
private const val SHAPE_PROPERTY_NAME = "c4:shape"
private const val SPRITE_PROPERTY_NAME = "c4:sprite"
private const val LEGEND_SPRITE_PROPERTY_NAME = "c4:legendSprite"
private const val LEGEND_TEXT_PROPERTY_NAME = "c4:legendText"
private const val TECHNOLOGY_PROPERTY_NAME = "c4:technology"

// TODO doucment

// @JsonCreator
@Suppress("LongParameterList")
fun createElementStyle(
    tag: String,
    backgroundColor: String? = null,
    fontColor: String? = null,
    border: Border? = null,
    borderWith: Int? = null,
    borderColor: String? = null,
    technology: String? = null,
    shadowing: Boolean = false,
    c4Shape: C4Shape? = null,
    sprite: Sprite? = null,
    legendText: String? = null,
    legendSprite: Sprite? = null,
): ElementStyle {
    val style = createElementStyleFromTag(tag)
    style.backgroundColor = backgroundColor
    style.fontColor = fontColor
    style.border = border
    style.borderWith = borderWith
    style.borderColor = borderColor
    style.technology = technology
    style.shadowing = shadowing
    style.c4Shape = c4Shape
    style.sprite = sprite
    style.legendSprite = legendSprite
    style.legendText = legendText
    return style
}

var ElementStyle.backgroundColor: String?
    get() = this.background
    private set(backgroundColor) {
        backgroundColor?.let { this.background = backgroundColor }
    }

var ElementStyle.fontColor: String?
    get() = this.color
    private set(fontColor) {
        fontColor?.let { this.color = fontColor }
    }

var ElementStyle.borderWith: Int?
    get() = this.strokeWidth
    private set(strokeWith) {
        strokeWith?.let { this.strokeWidth = strokeWith }
    }

var ElementStyle.borderColor: String?
    get() = this.properties[BORDER_COLOR_PROPERTY_NAME]
    private set(borderColor) {
        borderColor?.let { this.addProperty(BORDER_COLOR_PROPERTY_NAME, toValidColor(borderColor)) }
    }

var ElementStyle.technology: String?
    get() = this.properties[TECHNOLOGY_PROPERTY_NAME]
    private set(technology) {
        technology?.let { this.addProperty(TECHNOLOGY_PROPERTY_NAME, technology) }
    }

var ElementStyle.shadowing: Boolean
    get() = this.properties[SHADOWING_PROPERTY_NAME] == "true"
    private set(shadowing) {
        if (shadowing) {
            this.addProperty(SHADOWING_PROPERTY_NAME, "true")
        }
    }

var ElementStyle.c4Shape: C4Shape?
    get() = this.properties[SHAPE_PROPERTY_NAME]?.let { C4Shape.valueOf(it) }
    private set(c4Shape) {
        c4Shape?.let { this.addProperty(SHAPE_PROPERTY_NAME, c4Shape.toString()) }
    }

var ElementStyle.sprite: Sprite?
    get() = this.properties[SPRITE_PROPERTY_NAME]?.let { spriteFormJson(it) }
    private set(sprite) {
        sprite?.let { this.addProperty(SPRITE_PROPERTY_NAME, it.toJson()) }
    }

var ElementStyle.legendSprite: Sprite?
    get() = this.properties[LEGEND_SPRITE_PROPERTY_NAME]?.let { spriteFormJson(it) }
    private set(legendSprite) {
        legendSprite?.let { this.addProperty(LEGEND_SPRITE_PROPERTY_NAME, it.toJson()) }
    }

var ElementStyle.legendText: String?
    get() = this.properties[LEGEND_TEXT_PROPERTY_NAME]
    private set(legendText) {
        legendText?.let { this.addProperty(LEGEND_TEXT_PROPERTY_NAME, legendText) }
    }
