package com.github.chriskn.structurizrextension.api.view.sprite

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

private val mapper = jacksonObjectMapper()

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

    companion object {
        fun fromJson(json: String): Sprite = mapper.readValue(json)
    }

    val scale: Double?

    fun toJson(): String = mapper.writeValueAsString(this)
}
