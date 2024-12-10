package com.github.chriskn.structurizrextension.api.view.sprite.library

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.chriskn.structurizrextension.api.view.sprite.sprites.ImageSprite
import com.github.chriskn.structurizrextension.api.view.sprite.sprites.PlantUmlSprite
import com.github.chriskn.structurizrextension.api.view.sprite.sprites.Sprite
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Sprite library
 *
 * Library offering sprites from different [SpriteSet]s associated by their name.
 *
 * Allows to load json SpriteSets from URLs.
 */
object SpriteLibrary {

    private const val DEFAULT_SPRITES_FOLDER = "/sprites/"

    private val spritesByName: MutableMap<String, Sprite> = mutableMapOf()

    private val spriteSetNames = listOf(
        "aws_stdlib_sprites.json",
        "azure_stdlib_sprites.json",
        "cloudinsight_stdlib_sprites.json",
        "elastic_stdlib_sprites.json",
        "gcp_stdlib_sprites.json",
        "gilbarbara_image_sprites.json",
        "k8s_stdlib_sprites.json",
        "logos_stdlib_sprites.json",
        "material_stdlib_sprites.json",
        "office_stdlib_sprites.json",
        "osa_stdlib_sprites.json",
        "tupadr3_stdlib_sprites.json",
    )

    init {
        spriteSetNames.forEach { spriteSetName ->
            val spriteSetPath = DEFAULT_SPRITES_FOLDER + spriteSetName
            loadSpriteSet(spriteSetPath)
        }
    }

    /**
     * Get Sprite by name
     *
     * @param name the name of the sprite.
     * @return the sprite with the given name
     *
     * @throws IllegalArgumentException if sprite with name does not exist
     */
    fun spriteByName(name: String): Sprite {
        val lowercaseName = name.lowercase()
        return spritesByName[lowercaseName]
            ?: throw IllegalArgumentException(
                "No sprite found for name $lowercaseName. Possible matches: [${
                    findSpriteByNameContaining(Regex(lowercaseName))
                        .map { it.name }
                        .joinToString(", ")
                }]"
            )
    }

    /**
     * Get Sprite by name or null
     *
     * @param name the name of the sprite.
     * @return the sprite with the given name or null if no sprite with name exists
     */
    fun spriteByNameOrNull(name: String): Sprite? {
        val lowercaseName = name.lowercase()
        return spritesByName[lowercaseName]
    }

    /**
     * Find Sprite by name containing regex
     *
     * @param nameRegex the regex applied to all sprite names
     * @return all sprites with name containing nameRegex
     */
    fun findSpriteByNameContaining(nameRegex: Regex): List<Sprite> = spritesByName
        .filter { it.key.contains(nameRegex) }
        .values
        .toList()

    /**
     * Load sprite set
     *
     * Loads a [SpriteSet] json from the given URL and adds the contained sprites to the library
     *
     * @param spriteSetPath path pointing to [SpriteSet] json file
     * @return the loaded SpriteSet
     */
    fun loadSpriteSet(spriteSetPath: String): SpriteSet {
        val spriteSetStream = this.javaClass.getResourceAsStream(spriteSetPath)
            ?: throw IllegalArgumentException("SpriteSet not found under path: $spriteSetPath")
        BufferedReader(
            InputStreamReader(spriteSetStream, Charsets.UTF_8)
        ).use { reader ->
            val spriteSet = jacksonObjectMapper().readValue(reader.readText(), SpriteSet::class.java)
            val configuredSprites = spriteSet.sprites.map { sprite ->
                configureSprite(sprite, spriteSet)
            }.toSet()
            addSpritesByName(configuredSprites)
            return spriteSet.copy(sprites = configuredSprites)
        }
    }

    private fun addSpritesByName(sprites: Set<Sprite>) {
        val spritesWithName = sprites.filter { it.name != null }
        spritesByName.putAll(spritesWithName.associateBy { it.name!!.lowercase() })
    }

    private fun configureSprite(
        sprite: Sprite,
        spriteSet: SpriteSet,
    ) = when (sprite) {
        is PlantUmlSprite -> sprite.copy(
            additionalIncludes = spriteSet.additionalIncludes.orEmpty() + sprite.additionalIncludes.orEmpty(),
            additionalDefinitions = spriteSet.additionalDefinitions.orEmpty() + sprite.additionalDefinitions.orEmpty()
        )

        is ImageSprite -> sprite.copy(
            additionalDefinitions = spriteSet.additionalDefinitions.orEmpty() + sprite.additionalDefinitions.orEmpty()
        )

        else -> sprite
    }
}
