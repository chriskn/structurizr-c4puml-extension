package com.github.chriskn.structurizrextension.view.sprite

import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.api.view.style.sprite.ImageSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.OpenIconicSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.PumlSprite
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SpriteTest {

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
    inner class PumlSpriteTest {

        @Test
        fun `IllegalArgumentException is thrown when name is blank`() {
            val url = IconRegistry.iconUrlFor("kafka")!!
            assertThrows<IllegalArgumentException> {
                PumlSprite(name = "", includeUrl = url)
            }
        }

        @Test
        fun `IllegalArgumentException is thrown when url is blank`() {
            assertThrows<IllegalArgumentException> {
                PumlSprite(name = " ", includeUrl = " ")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown for invalid PumlSprite color`() {
            val name = IconRegistry.iconFileNameFor("kafka")!!
            val url = IconRegistry.iconUrlFor("kafka")!!

            assertThrows<IllegalArgumentException> {
                PumlSprite(name = name, includeUrl = url, color = "123")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown when url does not point to a puml file`() {
            assertThrows<IllegalArgumentException> {
                PumlSprite("test", "https://plantuml.com/logo.png")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown when url is invalid`() {
            assertThrows<IllegalArgumentException> {
                PumlSprite("test", "plantuml.com/logo.png")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown if scale is negative `() {
            assertThrows<IllegalArgumentException> {
                PumlSprite("test", "https://plantuml.com/logo.puml", scale = -0.1)
            }
        }

        @Test
        fun `IllegalArgumentException is thrown if scale is zero `() {
            assertThrows<IllegalArgumentException> {
                PumlSprite("test", "https://plantuml.com/logo.puml", scale = 0.0)
            }
        }
    }
}
