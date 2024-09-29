package com.github.chriskn.structurizrextension.api.view.style.sprite

data class OpenIconicSprite(
    val name: String,
    val color: String? = null,
    override val scale: Double? = null,
) : Sprite
