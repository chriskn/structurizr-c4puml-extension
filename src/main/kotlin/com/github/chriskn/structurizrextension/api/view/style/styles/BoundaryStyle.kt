package com.github.chriskn.structurizrextension.api.view.style.styles

import com.github.chriskn.structurizrextension.api.view.style.C4Shape
import com.github.chriskn.structurizrextension.api.view.style.sprite.Sprite
import com.github.chriskn.structurizrextension.api.view.style.toValidColor
import com.structurizr.view.Border

// TODO doucment

@Suppress("LongParameterList")
class BoundaryStyle internal constructor(
    override val tag: String,
    override val backgroundColor: String?,
    override val fontColor: String?,
    override val border: Border?,
    override val borderWith: Int?,
    override val borderColor: String?,
    override val shadowing: Boolean,
    override val c4Shape: C4Shape?,
    override val sprite: Sprite?,
    override val legendText: String?,
    override val legendSprite: Sprite?,
) : C4PumlStyle

@Suppress("LongParameterList")
fun createBoundaryStyle(
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
    legendSprite: Sprite? = null,
): BoundaryStyle {
    require(tag.isNotBlank()) { "tag must not be blank" }
    return BoundaryStyle(
        tag = tag,
        backgroundColor = toValidColor(backgroundColor),
        fontColor = toValidColor(fontColor),
        border = border,
        borderWith = borderWith,
        borderColor = toValidColor(borderColor),
        shadowing = shadowing,
        c4Shape = c4Shape,
        sprite = sprite,
        legendText = legendText,
        legendSprite = legendSprite,
    )
}
