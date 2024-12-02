package com.github.chriskn.structurizrextension.api.view.sprite.library

import com.github.chriskn.structurizrextension.api.view.sprite.sprites.Sprite

/**
 * SpriteSet
 *
 * Used to describe a set of sprites as json and load it via [SpriteLibrary]
 *
 * @property name                   name of the SpriteSet. Should describe the contained sprites
 * @property source                 optional url or other pointer to the original source of the sprites contained
 * @property sprites                set of the sprites included in this SpriteSet
 * @property additionalIncludes     set of additional includes that are added to all additional includes of
 *                                  PlantUmlSprites configured in this SpriteSet.
 * @property additionalDefinitions  set of additional definitions that are added to all additional definitions of
 *                                  PlantUmlSprites and ImageSprites configured in this SpriteSet.
 * @see com.github.chriskn.structurizrextension.api.view.sprite.PlantUmlSprite
 * @see com.github.chriskn.structurizrextension.api.view.sprite.ImageSprite
 */
data class SpriteSet(
    val name: String,
    val source: String?,
    val sprites: Set<Sprite>,
    val additionalIncludes: Set<String>?,
    val additionalDefinitions: Set<String>?,
)
