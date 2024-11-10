package com.github.chriskn.structurizrextension.view.style.person

import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.api.model.softwareSystem
import com.github.chriskn.structurizrextension.api.view.containerView
import com.github.chriskn.structurizrextension.api.view.sprite.ImageSprite
import com.github.chriskn.structurizrextension.api.view.sprite.OpenIconicSprite
import com.github.chriskn.structurizrextension.api.view.style.C4PUmlElementShape.EIGHT_SIDED
import com.github.chriskn.structurizrextension.api.view.style.C4PUmlElementShape.ROUNDED_BOX
import com.github.chriskn.structurizrextension.api.view.style.addPersonStyle
import com.github.chriskn.structurizrextension.api.view.style.getPersonStyles
import com.github.chriskn.structurizrextension.api.view.style.styles.C4PUmlLineStyle.DASHED
import com.github.chriskn.structurizrextension.api.view.style.styles.C4PUmlLineStyle.DOTTED
import com.github.chriskn.structurizrextension.api.view.style.styles.createPersonStyle
import com.github.chriskn.structurizrextension.internal.export.view.style.toJson
import com.structurizr.Workspace
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PersonStyleExtensionTest {

    @Test
    fun `person style values are correctly set`() {
        val expSprite = ImageSprite("img:https://plantuml.com/logo3.png", 0.4)
        val expLegendSprite = OpenIconicSprite("&compass", scale = 3.0, color = "blue")
        val expTag = "styleTag"
        val expBackgroundColor = "#000000"
        val expBorder = DASHED
        val expBorderWith = 4
        val expBorderColor = "#008000"
        val expFontColor = "#ffffff"
        val expShadowing = true
        val expC4Shape = EIGHT_SIDED
        val expLegendText = "this is a legend"

        val style = createPersonStyle(
            tag = expTag,
            backgroundColor = expBackgroundColor,
            borderStyle = expBorder,
            borderWidth = expBorderWith,
            borderColor = expBorderColor,
            fontColor = expFontColor,
            shadowing = expShadowing,
            c4Shape = expC4Shape,
            sprite = expSprite,
            legendSprite = expLegendSprite,
            legendText = expLegendText
        )

        assertThat(style.tag).isEqualTo(expTag)
        assertThat(style.backgroundColor).isEqualTo(expBackgroundColor)
        assertThat(style.borderStyle).isEqualTo(expBorder)
        assertThat(style.borderWidth).isEqualTo(expBorderWith)
        assertThat(style.borderColor).isEqualTo(expBorderColor)
        assertThat(style.fontColor).isEqualTo(expFontColor)
        assertThat(style.shadowing).isEqualTo(expShadowing)
        assertThat(style.c4Shape).isEqualTo(expC4Shape)
        assertThat(style.sprite).isEqualTo(expSprite)
        assertThat(style.legendSprite).isEqualTo(expLegendSprite)
        assertThat(style.legendText).isEqualTo(expLegendText)
    }

    @Test
    fun `person style can be added to ViewSet`() {
        val sprite = IconRegistry.spriteForName("postgresql")!!.copy(color = "green", scale = 0.5)
        val legendSprite = OpenIconicSprite("&compass")
        val style1 = createPersonStyle(
            tag = "tag",
            backgroundColor = "#ffffff",
            borderStyle = DOTTED,
            borderWidth = 5,
            borderColor = "purple",
            fontColor = "red",
            shadowing = false,
            c4Shape = ROUNDED_BOX,
            sprite = sprite,
            legendSprite = legendSprite,
            legendText = "this is a legend text"
        )
        val style2 = createPersonStyle("tag1")

        val workspace = Workspace("test", "test")
        val views = workspace.views

        views.addPersonStyle(style1)
        views.addPersonStyle(style2)

        val personStyles = views.getPersonStyles()
        assertThat(personStyles).hasSize(2)
        assertThat(personStyles.firstOrNull { it.tag == style1.tag }).isEqualTo(style1)
        assertThat(personStyles.firstOrNull { it.tag == style2.tag }).isEqualTo(style2)
    }

    @Test
    fun `person style can be added to View`() {
        val style1 = createPersonStyle(
            tag = "tag",
            backgroundColor = "#ffffff",
            borderStyle = DOTTED,
            borderWidth = 5,
        )
        val style2 = createPersonStyle("tag1")

        val workspace = Workspace("test", "test")
        val views = workspace.views

        val system = workspace.model.softwareSystem("test", "test")
        val view = views.containerView(system, "testview", "desc")
        view.addPersonStyle(style1)
        view.addPersonStyle(style2)

        val styles = view.getPersonStyles()
        assertThat(styles).hasSize(2)
        assertThat(styles.map { it.toJson() }).contains(style1.toJson(), style2.toJson())
    }

    @Test
    fun `IllegalArgumentException is thrown when tag is blank`() {
        assertThrows<IllegalArgumentException> {
            createPersonStyle(" ")
        }
    }

    @Test
    fun `person style can be initialized with null values`() {
        val expTag = "tag"

        val style = createPersonStyle(tag = expTag)

        assertThat(style.tag).isEqualTo(expTag)
        assertThat(style.backgroundColor).isNull()
        assertThat(style.borderStyle).isNull()
        assertThat(style.borderWidth).isNull()
        assertThat(style.borderColor).isNull()
        assertThat(style.fontColor).isNull()
        assertThat(style.shadowing).isFalse()
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
                createPersonStyle("test", backgroundColor = "ABC")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown for invalid font color`() {
            assertThrows<IllegalArgumentException> {
                createPersonStyle("test", fontColor = "jellow")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown for invalid border color`() {
            assertThrows<IllegalArgumentException> {
                createPersonStyle("test", borderColor = "")
            }
        }

        @Test
        fun `named border color is translated to hex color`() {
            val elementStyle = createPersonStyle("test", borderColor = "green")
            assertThat(elementStyle.borderColor).isEqualTo("#008000")
        }

        @Test
        fun `named font color is translated to hex color`() {
            val elementStyle = createPersonStyle("test", fontColor = "black")
            assertThat(elementStyle.fontColor).isEqualTo("#000000")
        }

        @Test
        fun `named background color is translated to hex color`() {
            val elementStyle = createPersonStyle("test", backgroundColor = "white")
            assertThat(elementStyle.backgroundColor).isEqualTo("#ffffff")
        }
    }
}
