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
interface Sprite {

    val scale: Double?
}
