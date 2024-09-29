package com.github.chriskn.structurizrextension.view.style

import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.api.model.softwareSystem
import com.github.chriskn.structurizrextension.api.view.containerView
import com.github.chriskn.structurizrextension.api.view.style.C4Shape.EIGHT_SIDED
import com.github.chriskn.structurizrextension.api.view.style.C4Shape.ROUNDED_BOX
import com.github.chriskn.structurizrextension.api.view.style.addElementStyle
import com.github.chriskn.structurizrextension.api.view.style.backgroundColor
import com.github.chriskn.structurizrextension.api.view.style.borderColor
import com.github.chriskn.structurizrextension.api.view.style.borderWith
import com.github.chriskn.structurizrextension.api.view.style.c4Shape
import com.github.chriskn.structurizrextension.api.view.style.createElementStyle
import com.github.chriskn.structurizrextension.api.view.style.fontColor
import com.github.chriskn.structurizrextension.api.view.style.getElementStyles
import com.github.chriskn.structurizrextension.api.view.style.legendSprite
import com.github.chriskn.structurizrextension.api.view.style.legendText
import com.github.chriskn.structurizrextension.api.view.style.shadowing
import com.github.chriskn.structurizrextension.api.view.style.sprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.ImageSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.OpenIconicSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.PumlSprite
import com.github.chriskn.structurizrextension.api.view.style.technology
import com.github.chriskn.structurizrextension.internal.export.view.style.toJson
import com.structurizr.Workspace
import com.structurizr.view.Border.Dashed
import com.structurizr.view.Border.Dotted
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ElementStyleExtensionTest {

    @Test
    fun `element style values are correctly set`() {
        val expSprite = ImageSprite("img:https://plantuml.com/logo3.png", 0.4)
        val expLegendSprite = OpenIconicSprite("compass", scale = 3.0, color = "blue")
        val expTag = "styleTag"
        val expBackgroundColor = "#000000"
        val expBorder = Dashed
        val expBorderWith = 4
        val expBorderColor = "#008000"
        val expFontColor = "#ffffff"
        val expShadowing = true
        val expTechnology = "Kafka"
        val expC4Shape = EIGHT_SIDED
        val expLegendText = "this is a legend"

        val style = createElementStyle(
            tag = expTag,
            backgroundColor = expBackgroundColor,
            border = expBorder,
            borderWith = expBorderWith,
            borderColor = expBorderColor,
            fontColor = expFontColor,
            shadowing = expShadowing,
            technology = expTechnology,
            c4Shape = expC4Shape,
            sprite = expSprite,
            legendSprite = expLegendSprite,
            legendText = expLegendText
        )

        assertThat(style.tag).isEqualTo(expTag)
        assertThat(style.backgroundColor).isEqualTo(expBackgroundColor)
        assertThat(style.border).isEqualTo(expBorder)
        assertThat(style.borderWith).isEqualTo(expBorderWith)
        assertThat(style.borderColor).isEqualTo(expBorderColor)
        assertThat(style.fontColor).isEqualTo(expFontColor)
        assertThat(style.shadowing).isEqualTo(expShadowing)
        assertThat(style.technology).isEqualTo(expTechnology)
        assertThat(style.c4Shape).isEqualTo(expC4Shape)
        assertThat(style.sprite).isEqualTo(expSprite)
        assertThat(style.legendSprite).isEqualTo(expLegendSprite)
        assertThat(style.legendText).isEqualTo(expLegendText)
    }

    @Test
    fun `element style can be added to ViewSet`() {
        val sprite = PumlSprite(
            includeUrl = IconRegistry.iconUrlFor("postgresql")!!,
            name = "postgresql",
            scale = 0.5,
            color = "green"
        )
        val legendSprite = OpenIconicSprite("compass")
        val style1 = createElementStyle(
            tag = "tag",
            backgroundColor = "#ffffff",
            border = Dotted,
            borderWith = 5,
            borderColor = "purple",
            fontColor = "red",
            shadowing = false,
            technology = "REST",
            c4Shape = ROUNDED_BOX,
            sprite = sprite,
            legendSprite = legendSprite,
            legendText = "this is a legend text"
        )
        val style2 = createElementStyle("tag1")

        val workspace = Workspace("test", "test")
        val views = workspace.views

        views.addElementStyle(style1)
        views.addElementStyle(style2)

        assertThat(views.getElementStyles()).hasSize(2)
        assertThat(views.getElementStyles()[0]).isEqualTo(style1)
        assertThat(views.getElementStyles()[1]).isEqualTo(style2)
    }

    @Test
    fun `element style can be added to View`() {
        val style1 = createElementStyle(
            tag = "tag",
            backgroundColor = "#ffffff",
            border = Dotted,
            borderWith = 5,
        )
        val style2 = createElementStyle("tag1")

        val workspace = Workspace("test", "test")
        val views = workspace.views

        val system = workspace.model.softwareSystem("test", "test")
        val view = views.containerView(system, "testview", "desc")
        view.addElementStyle(style1)
        view.addElementStyle(style2)

        val elementStyles = view.getElementStyles()
        assertThat(elementStyles).hasSize(2)
        assertThat(elementStyles.map { it.toJson() }).contains(style1.toJson(), style2.toJson())
    }

    @Test
    fun `IllegalArgumentException is thrown when tag is blank`() {
        assertThrows<IllegalArgumentException> {
            createElementStyle(" ")
        }
    }

    @Test
    fun `element style can be initialized with null values`() {
        val expTag = "tag"

        val style = createElementStyle(tag = expTag)

        assertThat(style.tag).isEqualTo(expTag)
        assertThat(style.backgroundColor).isNull()
        assertThat(style.border).isNull()
        assertThat(style.borderWith).isNull()
        assertThat(style.borderColor).isNull()
        assertThat(style.fontColor).isNull()
        assertThat(style.shadowing).isFalse()
        assertThat(style.technology).isNull()
        assertThat(style.c4Shape).isNull()
        assertThat(style.sprite).isNull()
        assertThat(style.legendSprite).isNull()
        assertThat(style.legendText).isNull()
    }

    @Nested
    inner class Color {

        @Test
        fun `IllegalArgumentException is thrown for invalid background color`() {
            assertThrows<IllegalArgumentException> {
                createElementStyle("test", backgroundColor = "ABC")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown for invalid font color`() {
            assertThrows<IllegalArgumentException> {
                createElementStyle("test", fontColor = "jellow")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown for invalid border color`() {
            assertThrows<IllegalArgumentException> {
                createElementStyle("test", borderColor = "")
            }
        }

        @Test
        fun `named border color is translated to hex color`() {
            val elementStyle = createElementStyle("test", borderColor = "green")
            assertThat(elementStyle.borderColor).isEqualTo("#008000")
        }

        @Test
        fun `named font color is translated to hex color`() {
            val elementStyle = createElementStyle("test", fontColor = "black")
            assertThat(elementStyle.fontColor).isEqualTo("#000000")
        }

        @Test
        fun `named background color is translated to hex color`() {
            val elementStyle = createElementStyle("test", backgroundColor = "white")
            assertThat(elementStyle.backgroundColor).isEqualTo("#ffffff")
        }
    }

    @Nested
    inner class Sprite {

        @Test
        fun `PumlSprite is serialized and deserialized correctly`() {
            val expectedSprite = PumlSprite(
                name = "android",
                includeUrl = "https://test.com/sprites/android-icon.puml",
            )
            val style = createElementStyle("test", sprite = expectedSprite)

            assertThat(style.sprite).isEqualTo(expectedSprite)
        }

        @Test
        fun `PumlSprite is serialized and deserialized correctly when color is used`() {
            val expectedSprite = PumlSprite(
                name = "android",
                includeUrl = "https://test.com/sprites/android-icon.puml",
                color = "green"
            )
            val style = createElementStyle("test", sprite = expectedSprite)

            assertThat(style.sprite).isEqualTo(expectedSprite)
        }

        @Test
        fun `PumlSprite is serialized and deserialized correctly when scale is used`() {
            val expectedSprite = PumlSprite(
                name = "android",
                includeUrl = "https://test.com/sprites/android-icon.puml",
                scale = 0.4
            )
            val style = createElementStyle("test", sprite = expectedSprite)

            assertThat(style.sprite).isEqualTo(expectedSprite)
        }

        @Test
        fun `PumlSprite is serialized and deserialized correctly when scale and color is used`() {
            val expectedSprite = PumlSprite(
                name = "android",
                includeUrl = "https://test.com/sprites/android-icon.puml",
                scale = 0.4,
                color = "green"
            )
            val style = createElementStyle("test", sprite = expectedSprite)

            assertThat(style.sprite).isEqualTo(expectedSprite)
        }

        @Test
        fun `OpenIconicSprite is serialized and deserialized correctly`() {
            val expectedSprite = OpenIconicSprite("folder")
            val style = createElementStyle("test", sprite = expectedSprite)

            assertThat(style.sprite).isEqualTo(expectedSprite)
        }

        @Test
        fun `OpenIconicSprite is serialized and deserialized correctly with scale`() {
            val expectedSprite = OpenIconicSprite("folder", scale = 0.4)
            val style = createElementStyle("test", sprite = expectedSprite)

            assertThat(style.sprite).isEqualTo(expectedSprite)
        }

        @Test
        fun `OpenIconicSprite is serialized and deserialized correctly with color`() {
            val expectedSprite = OpenIconicSprite("folder", color = "grey")
            val style = createElementStyle("test", sprite = expectedSprite)

            assertThat(style.sprite).isEqualTo(expectedSprite)
        }

        @Test
        fun `OpenIconicSprite is serialized and deserialized correctly with scale and color`() {
            val expectedSprite = OpenIconicSprite("folder", color = "grey", scale = 0.1)
            val style = createElementStyle("test", sprite = expectedSprite)

            assertThat(style.sprite).isEqualTo(expectedSprite)
        }

        @Test
        fun `ImageSprite is serialized and deserialized correctly when URI with scale is used`() {
            val expectedSprite = ImageSprite("img:https://plantuml.com/logo.png", 0.4)
            val style = createElementStyle("test", sprite = expectedSprite)

            assertThat(style.sprite).isEqualTo(expectedSprite)
        }

        @Test
        fun `ImageSprite is serialized and deserialized correctly when URI without scale is used`() {
            val expectedSprite = ImageSprite("img:https://plantuml.com/logo.png")
            val style = createElementStyle("test", sprite = expectedSprite)

            assertThat(style.sprite).isEqualTo(expectedSprite)
        }
    }
}
