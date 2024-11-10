package com.github.chriskn.structurizrextension.api.view.style.styles

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.chriskn.structurizrextension.api.view.sprite.Sprite
import com.github.chriskn.structurizrextension.api.view.style.C4PUmlElementShape
import com.github.chriskn.structurizrextension.internal.view.style.toValidColor

/**
 * Base class for model element styles
 */
abstract class ModelElementStyle {
    abstract val tag: String
    abstract val backgroundColor: String?
    abstract val fontColor: String?
    abstract val borderStyle: C4PUmlLineStyle?
    abstract val borderWidth: Int?
    abstract val borderColor: String?
    abstract val shadowing: Boolean
    abstract val c4Shape: C4PUmlElementShape?
    abstract val sprite: Sprite?
    abstract val legendText: String?
    abstract val legendSprite: Sprite?

    @get:JsonIgnore
    internal var borderColorValidated: String? = null
        private set(value) {
            field = toValidColor(value)
        }

    @get:JsonIgnore
    internal var backgroundColorValidated: String? = null
        private set(value) {
            field = toValidColor(value)
        }

    @get:JsonIgnore
    internal var fontColorValidated: String? = null
        private set(value) {
            field = toValidColor(value)
        }

    internal fun validate() {
        require(tag.isNotBlank()) { "tag must not be blank" }
        backgroundColorValidated = backgroundColor
        borderColorValidated = borderColor
        fontColorValidated = fontColor
    }
}
