package com.github.chriskn.structurizrextension.view.style.element

import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.api.model.softwareSystem
import com.github.chriskn.structurizrextension.api.view.containerView
import com.github.chriskn.structurizrextension.api.view.style.C4PUmlElementShape.EIGHT_SIDED
import com.github.chriskn.structurizrextension.api.view.style.C4PUmlElementShape.ROUNDED_BOX
import com.github.chriskn.structurizrextension.api.view.style.addElementStyle
import com.github.chriskn.structurizrextension.api.view.style.getElementStyles
import com.github.chriskn.structurizrextension.api.view.style.sprite.ImageSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.OpenIconicSprite
import com.github.chriskn.structurizrextension.api.view.style.styles.C4PUmlLineStyle.DASHED
import com.github.chriskn.structurizrextension.api.view.style.styles.C4PUmlLineStyle.DOTTED
import com.github.chriskn.structurizrextension.api.view.style.styles.ElementStyle
import com.github.chriskn.structurizrextension.internal.export.view.style.toJson
import com.structurizr.Workspace
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
        val expBorder = DASHED
        val expBorderWidth = 4
        val expBorderColor = "#008000"
        val expFontColor = "#ffffff"
        val expShadowing = true
        val expTechnology = "Kafka"
        val expC4Shape = EIGHT_SIDED
        val expLegendText = "this is a legend"

        val style = ElementStyle(
            tag = expTag,
            backgroundColor = expBackgroundColor,
            borderStyle = expBorder,
            borderWidth = expBorderWidth,
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
        assertThat(style.borderStyle).isEqualTo(expBorder)
        assertThat(style.borderWidth).isEqualTo(expBorderWidth)
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
        val sprite = IconRegistry.spriteForName("postgresql")!!.copy(color = "green", scale = 0.5)
        val legendSprite = OpenIconicSprite("compass")
        val style1 = ElementStyle(
            tag = "tag",
            backgroundColor = "#ffffff",
            borderStyle = DOTTED,
            borderWidth = 5,
            borderColor = "purple",
            fontColor = "red",
            shadowing = false,
            technology = "REST",
            c4Shape = ROUNDED_BOX,
            sprite = sprite,
            legendSprite = legendSprite,
            legendText = "this is a legend text"
        )
        val style2 = ElementStyle("tag1")

        val workspace = Workspace("test", "test")
        val views = workspace.views

        views.addElementStyle(style1)
        views.addElementStyle(style2)

        assertThat(views.getElementStyles()).hasSize(2)
        assertThat(views.getElementStyles().firstOrNull { it.tag == style1.tag }).isEqualTo(style1)
        assertThat(views.getElementStyles().firstOrNull { it.tag == style2.tag }).isEqualTo(style2)
    }

    @Test
    fun `element style can be added to View`() {
        val style1 = ElementStyle(
            tag = "tag",
            backgroundColor = "#ffffff",
            borderStyle = DOTTED,
            borderWidth = 5,
        )
        val style2 = ElementStyle("tag1")

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
            ElementStyle(" ")
        }
    }

    @Test
    fun `element style can be initialized with null values`() {
        val expTag = "tag"

        val style = ElementStyle(tag = expTag)

        assertThat(style.tag).isEqualTo(expTag)
        assertThat(style.backgroundColor).isNull()
        assertThat(style.borderStyle).isNull()
        assertThat(style.borderWidth).isNull()
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
                ElementStyle("test", backgroundColor = "ABC")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown for invalid font color`() {
            assertThrows<IllegalArgumentException> {
                ElementStyle("test", fontColor = "jellow")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown for invalid border color`() {
            assertThrows<IllegalArgumentException> {
                ElementStyle("test", borderColor = "")
            }
        }

        @Test
        fun `named border color is translated to hex color`() {
            val elementStyle = ElementStyle("test", borderColor = "green")
            assertThat(elementStyle.borderColorValidated).isEqualTo("#008000")
        }

        @Test
        fun `named font color is translated to hex color`() {
            val elementStyle = ElementStyle("test", fontColor = "black")
            assertThat(elementStyle.fontColorValidated).isEqualTo("#000000")
        }

        @Test
        fun `named background color is translated to hex color`() {
            val elementStyle = ElementStyle("test", backgroundColor = "white")
            assertThat(elementStyle.backgroundColorValidated).isEqualTo("#ffffff")
        }
    }
}
