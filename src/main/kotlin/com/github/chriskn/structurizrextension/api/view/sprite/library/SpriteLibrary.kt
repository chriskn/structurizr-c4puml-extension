package com.github.chriskn.structurizrextension.api.view.sprite.library

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.chriskn.structurizrextension.api.view.sprite.sprites.ImageSprite
import com.github.chriskn.structurizrextension.api.view.sprite.sprites.PlantUmlSprite
import com.github.chriskn.structurizrextension.api.view.sprite.sprites.Sprite
import java.net.URI
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.toPath

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

    private val defaultSpriteSetPaths = this.javaClass.getResource(DEFAULT_SPRITES_FOLDER)
        ?.toURI()
        ?.toPath()
        ?.listDirectoryEntries()
        .orEmpty()

    init {
        defaultSpriteSetPaths.map { spriteSetPath ->
            loadSpriteSet(spriteSetPath.toUri())
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
     * @param spriteSetJsonUri URI pointing to [SpriteSet] json file
     * @return the loaded SpriteSet
     */
    fun loadSpriteSet(spriteSetJsonUri: URI): SpriteSet {
        val spriteSet = jacksonObjectMapper().readValue(spriteSetJsonUri.toURL(), SpriteSet::class.java)
        val configuredSprites = spriteSet.sprites.map { sprite ->
            configureSprite(sprite, spriteSet)
        }.toSet()
        addSpritesByName(configuredSprites)
        return spriteSet.copy(sprites = configuredSprites)
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
