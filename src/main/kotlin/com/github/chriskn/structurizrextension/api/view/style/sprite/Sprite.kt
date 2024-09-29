package com.github.chriskn.structurizrextension.api.view.style.sprite

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
        Type(value = PumlSprite::class, name = "PumlSprite"),
    ]
)
abstract class Sprite(scale: Double?) {

    init {
        require((scale ?: 1.0) > 0.0) {
            "Scale must be greater than zero."
        }
    }
}
