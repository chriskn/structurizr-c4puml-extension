package com.github.chriskn.structurizrextension.api.view.style.styles

import com.github.chriskn.structurizrextension.api.view.style.C4Shape
import com.github.chriskn.structurizrextension.api.view.style.sprite.Sprite
import com.github.chriskn.structurizrextension.api.view.style.toValidColor
import com.structurizr.view.Border

// TODO doucment

class BoundaryStyle : C4PumlStyle {

    override val tag: String
    override val backgroundColor: String?
    override val fontColor: String?
    override val border: Border?
    override val borderWith: Int?
    override val borderColor: String?
    override val shadowing: Boolean
    override val c4Shape: C4Shape?
    override val sprite: Sprite?
    override val legendText: String?
    override val legendSprite: Sprite?

    @Suppress("LongParameterList")
    constructor(
        tag: String,
        backgroundColor: String? = null,
        fontColor: String? = null,
        border: Border? = null,
        borderWith: Int? = null,
        borderColor: String? = null,
        shadowing: Boolean = false,
        c4Shape: C4Shape? = null,
        sprite: Sprite? = null,
        legendText: String? = null,
        legendSprite: Sprite? = null
    ) {
        require(tag.isNotBlank()) { "tag cannot be blank" }
        this.tag = tag
        this.backgroundColor = toValidColor(backgroundColor)
        this.fontColor = toValidColor(fontColor)
        this.border = border
        this.borderWith = borderWith
        this.borderColor = toValidColor(borderColor)
        this.shadowing = shadowing
        this.c4Shape = c4Shape
        this.sprite = sprite
        this.legendText = legendText
        this.legendSprite = legendSprite
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BoundaryStyle

        if (tag != other.tag) return false
        if (backgroundColor != other.backgroundColor) return false
        if (fontColor != other.fontColor) return false
        if (border != other.border) return false
        if (borderWith != other.borderWith) return false
        if (borderColor != other.borderColor) return false
        if (shadowing != other.shadowing) return false
        if (c4Shape != other.c4Shape) return false
        if (sprite != other.sprite) return false
        if (legendText != other.legendText) return false
        if (legendSprite != other.legendSprite) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tag.hashCode()
        result = 31 * result + (backgroundColor?.hashCode() ?: 0)
        result = 31 * result + (fontColor?.hashCode() ?: 0)
        result = 31 * result + (border?.hashCode() ?: 0)
        result = 31 * result + (borderWith ?: 0)
        result = 31 * result + (borderColor?.hashCode() ?: 0)
        result = 31 * result + shadowing.hashCode()
        result = 31 * result + (c4Shape?.hashCode() ?: 0)
        result = 31 * result + (sprite?.hashCode() ?: 0)
        result = 31 * result + (legendText?.hashCode() ?: 0)
        result = 31 * result + (legendSprite?.hashCode() ?: 0)
        return result
    }
}
