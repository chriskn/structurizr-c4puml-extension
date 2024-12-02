package com.github.chriskn.structurizrextension.view.sprite.sprites

import com.github.chriskn.structurizrextension.api.view.sprite.sprites.ImageSprite
import com.github.chriskn.structurizrextension.api.view.sprite.sprites.OpenIconicSprite
import com.github.chriskn.structurizrextension.api.view.sprite.sprites.PlantUmlSprite
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SpriteTest {

    @Nested
    inner class ImageSpriteTest {

        @Test
        fun `IllegalArgumentException is thrown when url is blank`() {
            assertThrows<IllegalArgumentException> {
                ImageSprite(" ")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown when url is empty`() {
            assertThrows<IllegalArgumentException> {
                ImageSprite("")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown when url is invalid`() {
            assertThrows<IllegalArgumentException> {
                ImageSprite("test")
            }
        }

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
                ImageSprite(url = "img:https://plantuml.com/logo.png", scale = -0.1)
            }
        }

        @Test
        fun `IllegalArgumentException is thrown if scale is zero `() {
            assertThrows<IllegalArgumentException> {
                ImageSprite(url = "img:https://plantuml.com/logo.png", scale = 0.0)
            }
        }
    }

    @Nested
    inner class OpenIconicSpriteTest {

        @Test
        fun `IllegalArgumentException is thrown for invalid OpenIconicSprite color`() {
            assertThrows<IllegalArgumentException> {
                OpenIconicSprite("&folder", color = "123")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown for name not starting with &`() {
            assertThrows<IllegalArgumentException> {
                OpenIconicSprite("folder")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown for name only consisting of &`() {
            assertThrows<IllegalArgumentException> {
                OpenIconicSprite("&")
            }
        }
    }

    @Nested
    inner class PlantUmlSpriteTest {

        @Test
        fun `IllegalArgumentException is thrown when name is blank`() {
            assertThrows<IllegalArgumentException> {
                PlantUmlSprite("", "https://plantuml.com/logo.png")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown when url is blank`() {
            assertThrows<IllegalArgumentException> {
                PlantUmlSprite(name = " ", path = " ")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown for invalid PumlSprite color`() {
            assertThrows<IllegalArgumentException> {
                PlantUmlSprite("test", "https://plantuml.com/logo.puml", color = "123")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown when url does not point to a file`() {
            assertThrows<IllegalArgumentException> {
                PlantUmlSprite("test", "https://plantuml.com/logo.png")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown when url is invalid`() {
            assertThrows<IllegalArgumentException> {
                PlantUmlSprite("test", "plantuml.com/logo.png")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown if scale is negative `() {
            assertThrows<IllegalArgumentException> {
                PlantUmlSprite("test", "https://plantuml.com/logo.puml", scale = -0.1)
            }
        }

        @Test
        fun `IllegalArgumentException is thrown if scale is zero `() {
            assertThrows<IllegalArgumentException> {
                PlantUmlSprite("test", "https://plantuml.com/logo.puml", scale = 0.0)
            }
        }
    }
}
