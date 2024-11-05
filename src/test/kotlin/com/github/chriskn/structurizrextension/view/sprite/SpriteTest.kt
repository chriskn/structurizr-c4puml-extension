package com.github.chriskn.structurizrextension.view.sprite

import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.api.view.style.sprite.ImageSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.OpenIconicSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.PUmlSprite
import com.github.chriskn.structurizrextension.api.view.style.styles.ElementStyle
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SpriteTest {

    @Nested
    inner class Sprite {

        @Test
        fun `PumlSprite is serialized and deserialized correctly`() {
            val expectedSprite = PUmlSprite(
                name = "some technology",
                url = "https://test.com/sprites/android-icon.puml",
            )
            val style = ElementStyle("test", sprite = expectedSprite)

            assertThat(style.sprite).isEqualTo(expectedSprite)
        }

        @Test
        fun `PumlSprite is serialized and deserialized correctly when color is used`() {
            val expectedSprite = PUmlSprite(
                name = "some technology",
                url = "https://test.com/sprites/android-icon.puml",
                color = "green"
            )
            val style = ElementStyle("test", sprite = expectedSprite)

            assertThat(style.sprite).isEqualTo(expectedSprite)
        }

        @Test
        fun `PumlSprite is serialized and deserialized correctly when scale is used`() {
            val expectedSprite = PUmlSprite(
                name = "some technology",
                url = "https://test.com/sprites/android-icon.puml",
                scale = 0.4
            )
            val style = ElementStyle("test", sprite = expectedSprite)

            assertThat(style.sprite).isEqualTo(expectedSprite)
        }

        @Test
        fun `PumlSprite is serialized and deserialized correctly when scale and color is used`() {
            val expectedSprite = PUmlSprite(
                name = "some technology",
                url = "https://test.com/sprites/android-icon.puml",
                scale = 0.4,
                color = "green"
            )
            val style = ElementStyle("test", sprite = expectedSprite)

            assertThat(style.sprite).isEqualTo(expectedSprite)
        }

        @Test
        fun `OpenIconicSprite is serialized and deserialized correctly`() {
            val expectedSprite = OpenIconicSprite("folder")
            val style = ElementStyle("test", sprite = expectedSprite)

            assertThat(style.sprite).isEqualTo(expectedSprite)
        }

        @Test
        fun `OpenIconicSprite is serialized and deserialized correctly with scale`() {
            val expectedSprite = OpenIconicSprite("folder", scale = 0.4)
            val style = ElementStyle("test", sprite = expectedSprite)

            assertThat(style.sprite).isEqualTo(expectedSprite)
        }

        @Test
        fun `OpenIconicSprite is serialized and deserialized correctly with color`() {
            val expectedSprite = OpenIconicSprite("folder", color = "grey")
            val style = ElementStyle("test", sprite = expectedSprite)

            assertThat(style.sprite).isEqualTo(expectedSprite)
        }

        @Test
        fun `OpenIconicSprite is serialized and deserialized correctly with scale and color`() {
            val expectedSprite = OpenIconicSprite("folder", color = "grey", scale = 0.1)
            val style = ElementStyle("test", sprite = expectedSprite)

            assertThat(style.sprite).isEqualTo(expectedSprite)
        }
    }

    @Nested
    inner class ImageSpriteTest {

        @Test
        fun `IllegalArgumentException is thrown when uri does not point to file`() {
            assertThrows<IllegalArgumentException> {
                ImageSprite("https://plantuml.com/logo")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown when uri without image schema is used`() {
            assertThrows<IllegalArgumentException> {
                ImageSprite("https://plantuml.com/logo.png")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown when uri with different schema is used`() {
            assertThrows<IllegalArgumentException> {
                ImageSprite("file:https://plantuml.com/logo.png")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown if scale is negative `() {
            assertThrows<IllegalArgumentException> {
                ImageSprite("img:https://plantuml.com/logo.png", -0.1)
            }
        }

        @Test
        fun `IllegalArgumentException is thrown if scale is zero `() {
            assertThrows<IllegalArgumentException> {
                ImageSprite("img:https://plantuml.com/logo.png", 0.0)
            }
        }
    }

    @Nested
    inner class OpenIconicSpriteTest {

        @Test
        fun `IllegalArgumentException is thrown for invalid OpenIconicSprite color`() {
            assertThrows<IllegalArgumentException> {
                OpenIconicSprite("folder", color = "123")
            }
        }
    }

    @Nested
    inner class PUmlSpriteTest {

        @Test
        fun `IllegalArgumentException is thrown when name is blank`() {
            val url = IconRegistry.iconUrlFor("kafka")!!
            assertThrows<IllegalArgumentException> {
                PUmlSprite(name = "", url = url)
            }
        }

        @Test
        fun `IllegalArgumentException is thrown when url is blank`() {
            assertThrows<IllegalArgumentException> {
                PUmlSprite(name = " ", url = " ")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown for invalid PumlSprite color`() {
            val sprite = IconRegistry.spriteForName("kafka")!!

            assertThrows<IllegalArgumentException> {
                PUmlSprite(name = sprite.name, url = sprite.url, color = "123")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown when url does not point to a puml file`() {
            assertThrows<IllegalArgumentException> {
                PUmlSprite("test", "https://plantuml.com/logo.png")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown when url is invalid`() {
            assertThrows<IllegalArgumentException> {
                PUmlSprite("test", "plantuml.com/logo.png")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown if scale is negative `() {
            assertThrows<IllegalArgumentException> {
                PUmlSprite("test", "https://plantuml.com/logo.puml", scale = -0.1)
            }
        }

        @Test
        fun `IllegalArgumentException is thrown if scale is zero `() {
            assertThrows<IllegalArgumentException> {
                PUmlSprite("test", "https://plantuml.com/logo.puml", scale = 0.0)
            }
        }
    }
}
