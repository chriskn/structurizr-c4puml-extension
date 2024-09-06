package com.github.chriskn.structurizrextension.api.view.style

import java.net.URI

class Sprite {

    private val scale: Double?
    private val imageUri: URI?
    private val openIconicIconName: String?

    constructor(
        imageUrl: URI,
        scale: Double? = null,
    ) {
        validateScale(scale)
        this.scale = scale
        this.imageUri = imageUrl
        this.openIconicIconName = null
    }

    // https://www.plantuml.com/plantuml/uml/SoWkIImgAStDuSh9B2x9BqZDoqpE1s8kXzIy590m0000
    constructor(
        openIconicIconName: String,
        scale: Double? = null,
    ) {
        validateScale(scale)
        this.scale = scale
        this.openIconicIconName = openIconicIconName
        this.imageUri = null
    }

    override fun toString(): String {
        val spritePath = imageUri?.toString() ?: "&$openIconicIconName"
        return "$spritePath{scale=$scale}"
    }

    private fun validateScale(scale: Double?) {
        require(scale == null || scale > 0) {
            throw IllegalArgumentException("Sprite scale can not be negative")
        }
    }
}
