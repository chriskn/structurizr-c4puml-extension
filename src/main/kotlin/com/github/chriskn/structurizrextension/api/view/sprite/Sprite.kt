package com.github.chriskn.structurizrextension.api.view.sprite

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "@type"
)
@JsonSubTypes(
    value = [
        Type(value = ImageSprite::class, name = "ImageSprite"),
        Type(value = OpenIconicSprite::class, name = "OpenIconicSprite"),
        Type(value = PlantUmlSprite::class, name = "PlantUmlSprite"),
    ]
)
/**
 * Base class for Sprites
 */
sealed class Sprite(open val name: String? = null, scale: Double?) {

    init {
        require((scale ?: 1.0) > 0.0) {
            "Scale must be greater than zero."
        }
    }

    internal fun additionalDefinitions(): List<String> = when (this) {
        is PlantUmlSprite -> this.additionalDefinitions.orEmpty()
        is ImageSprite -> this.additionalDefinitions.orEmpty()
        is OpenIconicSprite -> emptyList()
    }

    internal fun additionalIncludes(): List<String> = when (this) {
        is PlantUmlSprite -> this.additionalIncludes.orEmpty()
        is ImageSprite -> emptyList()
        is OpenIconicSprite -> emptyList()
    }
}
