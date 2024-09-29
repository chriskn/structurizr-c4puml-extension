package com.github.chriskn.structurizrextension.api.view.style.sprite

data class ImageSprite(
    val url: String,
    override val scale: Double? = null,
) : Sprite
