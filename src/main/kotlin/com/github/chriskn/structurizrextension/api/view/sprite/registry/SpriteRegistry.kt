package com.github.chriskn.structurizrextension.api.view.sprite.registry

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.chriskn.structurizrextension.api.view.sprite.ImageSprite
import com.github.chriskn.structurizrextension.api.view.sprite.PlantUmlSprite
import com.github.chriskn.structurizrextension.api.view.sprite.Sprite
import java.net.URL

object SpriteRegistry {
    private const val SPRITE_FOLDER = "/sprites/"

    private val sprites: MutableMap<String, Sprite> = mutableMapOf()

    private val defaultSpritePaths = listOf(
        "${SPRITE_FOLDER}aws_stdlib_sprites.json",
        "${SPRITE_FOLDER}azure_stdlib_sprites.json",
        "${SPRITE_FOLDER}cloudinsight_stdlib_sprites.json",
        "${SPRITE_FOLDER}elastic_stdlib_sprites.json",
        "${SPRITE_FOLDER}gcp_stdlib_sprites.json",
        "${SPRITE_FOLDER}k8s_stdlib_sprites.json",
        "${SPRITE_FOLDER}logos_stdlib_sprites.json",
        "${SPRITE_FOLDER}material_stdlib_sprites.json",
        "${SPRITE_FOLDER}office_stdlib_sprites.json",
        "${SPRITE_FOLDER}osa_stdlib_sprites.json",
        "${SPRITE_FOLDER}tupadr3_stdlib_sprites.json",
        "${SPRITE_FOLDER}gilbarbara_image_sprites.json"
    )

    init {
        defaultSpritePaths.forEach { spriteSet ->
            this.javaClass.getResource(spriteSet)?.let { url -> loadSpriteSet(url) }
        }
    }

    private fun loadSpriteSet(spriteSetJsonUrl: URL): SpriteSet {
        val spriteSet = jacksonObjectMapper().readValue(spriteSetJsonUrl, SpriteSet::class.java)
        val configuredSprites = spriteSet.sprites.map { sprite ->
            configureSprite(sprite, spriteSet)
        }.toSet()
        val spritesByName = configuredSprites.filter { it.name != null }.associateBy { it.name!!.lowercase() }
        sprites.putAll(spritesByName)
        return spriteSet.copy(sprites = configuredSprites)
    }

    private fun configureSprite(
        sprite: Sprite,
        spriteSet: SpriteSet,
    ) = when (sprite) {
        is PlantUmlSprite -> sprite.copy(
            additionalIncludes = sprite.additionalIncludes.orEmpty() + spriteSet.additionalIncludes.orEmpty(),
            additionalDefinitions = sprite.additionalDefinitions.orEmpty() + spriteSet.additionalDefinitions.orEmpty()
        )

        is ImageSprite -> sprite.copy(
            additionalDefinitions = sprite.additionalDefinitions.orEmpty() + spriteSet.additionalDefinitions.orEmpty()
        )

        else ->
            sprite
    }

    fun spriteByName(name: String): Sprite = sprites[name.lowercase()]
        ?: throw IllegalArgumentException(
            "No sprite found for name ${name.lowercase()}. Possible matches: ${
                findSpriteContaining(
                    Regex(name.lowercase())
                ).map { it.name }
            }"
        )

    fun findSpriteContaining(regex: Regex): List<Sprite> = sprites
        .filter { it.key.contains(regex) }
        .values
        .toList()
}
