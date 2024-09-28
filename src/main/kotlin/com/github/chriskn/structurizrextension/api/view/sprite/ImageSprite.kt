package com.github.chriskn.structurizrextension.api.view.sprite

data class ImageSprite(
    val url: String,
    override val scale: Double? = null,
) : Sprite
