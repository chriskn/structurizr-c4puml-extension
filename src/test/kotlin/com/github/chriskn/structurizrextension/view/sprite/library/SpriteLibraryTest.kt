package com.github.chriskn.structurizrextension.view.sprite.library

import com.github.chriskn.structurizrextension.api.view.sprite.library.SpriteLibrary
import com.github.chriskn.structurizrextension.api.view.sprite.sprites.ImageSprite
import com.github.chriskn.structurizrextension.api.view.sprite.sprites.OpenIconicSprite
import com.github.chriskn.structurizrextension.api.view.sprite.sprites.PlantUmlSprite
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.io.FileNotFoundException
import java.net.URI

private const val EXISTING_SPRITE_NAME = "logos-DocKer-Icon"

class SpriteLibraryTest {

    @Test
    fun `spriteByName retrieves sprite by name`() {
        val sprite = SpriteLibrary.spriteByName(EXISTING_SPRITE_NAME)

        assertThat(sprite.name).isEqualToIgnoringCase(EXISTING_SPRITE_NAME.lowercase())
    }

    @Test
    fun `spriteByName throws IllegalArgumentException if name doesn't exist`() {
        assertThrows<IllegalArgumentException> {
            SpriteLibrary.spriteByName("aws")
        }
    }

    @Test
    fun `spriteByNameOrNull retrieves sprite by name`() {
        val sprite = SpriteLibrary.spriteByNameOrNull(EXISTING_SPRITE_NAME)

        assertThat(sprite!!.name).isEqualToIgnoringCase(EXISTING_SPRITE_NAME.lowercase())
    }

    @Test
    fun `spriteByNameOrNull return null if name doesn't exist`() {
        assertThat(SpriteLibrary.spriteByNameOrNull("aws")).isNull()
    }

    @Test
    fun `findSpriteByNameContaining filters sprites by name`() {
        val searchRegex = Regex("kafka")

        val sprites = SpriteLibrary.findSpriteByNameContaining(searchRegex)

        assertThat(sprites).hasSize(8)
    }

    @Test
    fun `findSpriteByNameContaining returns empty list if name doesn't exist`() {
        assertThat(SpriteLibrary.findSpriteByNameContaining(Regex("adsdsfds"))).isEmpty()
    }

    @Test
    fun `loadSpriteSet loads minimal test sprite set`() {
        val expectedSpriteName = "TestSprite"
        val spriteSetPath = this.javaClass.getResource("/input/sprites/minimal_sprite_set.json")!!

        val spriteSet = SpriteLibrary.loadSpriteSet(spriteSetPath.toURI())
        assertThat(spriteSet.sprites).hasSize(1)

        val spriteFromLibrary = SpriteLibrary.spriteByName(expectedSpriteName)
        val spriteFromSpriteSet = spriteSet.sprites.first { it.name == expectedSpriteName }
        assertThat(spriteFromLibrary).isNotNull
        assertThat(spriteFromSpriteSet).isNotNull
        assertThat(spriteFromLibrary).isEqualTo(spriteFromSpriteSet)
    }

    @Test
    fun `loadSpriteSet loads PlantUmlSprites as expected`() {
        val expectedPlantUmlSprite = PlantUmlSprite(
            name = "TestPlantUmlSprite",
            path = "<awslib14/AWSC4Integration>",
            color = "#eeeeee",
            scale = 0.5,
            additionalDefinitions = setOf("common_definition", "local_definition"),
            additionalIncludes = setOf("common_include.puml", "<local_include>"),
            reference = "test_reference"
        )
        val spriteSetPath = this.javaClass.getResource("/input/sprites/max_sprite_set.json")!!

        val spriteSet = SpriteLibrary.loadSpriteSet(spriteSetPath.toURI())
        val plantUmlSprite = spriteSet.sprites.first { it.name == expectedPlantUmlSprite.name }
        val plantUmlSpriteFromLib = SpriteLibrary.spriteByName(expectedPlantUmlSprite.name)
        assertThat(plantUmlSprite).isEqualTo(plantUmlSpriteFromLib)
        assertThat(plantUmlSpriteFromLib).isEqualTo(expectedPlantUmlSprite)
    }

    @Test
    fun `loadSpriteSet loads ImageSprites as expected`() {
        val expectedImageSprite = ImageSprite(
            name = "TestImageSprite",
            url = "img:someDefinition/active-campaign-icon.png",
            scale = 1.5,
            additionalDefinitions = setOf("common_definition", "local_definition_img"),
        )
        val spriteSetPath = this.javaClass.getResource("/input/sprites/max_sprite_set.json")!!

        val spriteSet = SpriteLibrary.loadSpriteSet(spriteSetPath.toURI())
        val imageSprite = spriteSet.sprites.first { it.name == expectedImageSprite.name }
        val imageSpriteFromLib = SpriteLibrary.spriteByName(expectedImageSprite.name!!)
        assertThat(imageSprite).isEqualTo(imageSpriteFromLib)
        assertThat(imageSpriteFromLib).isEqualTo(expectedImageSprite)
    }

    @Test
    fun `loadSpriteSet loads OpenIconicSprite as expected`() {
        val expectedOpenIconicSprite = OpenIconicSprite(
            name = "&compass",
            color = "#ffffff",
            scale = 1.2
        )
        val spriteSetPath = this.javaClass.getResource("/input/sprites/max_sprite_set.json")!!

        val spriteSet = SpriteLibrary.loadSpriteSet(spriteSetPath.toURI())
        val openIconicSprite = spriteSet.sprites.first { it.name == expectedOpenIconicSprite.name }
        val openIconicSpriteFromLib = SpriteLibrary.spriteByName(expectedOpenIconicSprite.name)
        assertThat(openIconicSprite).isEqualTo(openIconicSpriteFromLib)
        assertThat(openIconicSpriteFromLib).isEqualTo(expectedOpenIconicSprite)
    }

    @Test
    fun `loadSpriteSet returns ImageSprites without name`() {
        val sprites = SpriteLibrary.loadSpriteSet(
            this.javaClass.getResource("/input/sprites/unnamed_image_sprite_sprite_set.json")!!.toURI()
        )
        assertThat(sprites.sprites).hasSize(1)
    }

    @Test
    fun `loadSpriteSet throws Exception when url cant be resolved`() {
        // <java.net.ConnectException> vs <java.io.FileNotFoundException> depending on jvm version
        assertThrows<Exception> {
            SpriteLibrary.loadSpriteSet(URI("http://localhost/not_existing"))
        }
    }

    @Test
    fun `loadSpriteSet throws FileNotFoundException when no file exists under path`() {
        assertThrows<FileNotFoundException> {
            SpriteLibrary.loadSpriteSet(File("/input/sprites/").toURI())
        }
    }
}
