package com.github.chriskn.structurizrextension.view.style.boundary

import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.api.model.softwareSystem
import com.github.chriskn.structurizrextension.api.view.containerView
import com.github.chriskn.structurizrextension.api.view.sprite.ImageSprite
import com.github.chriskn.structurizrextension.api.view.sprite.OpenIconicSprite
import com.github.chriskn.structurizrextension.api.view.sprite.PlantUmlSprite
import com.github.chriskn.structurizrextension.api.view.style.C4PUmlElementShape.EIGHT_SIDED
import com.github.chriskn.structurizrextension.api.view.style.C4PUmlElementShape.ROUNDED_BOX
import com.github.chriskn.structurizrextension.api.view.style.addBoundaryStyle
import com.github.chriskn.structurizrextension.api.view.style.getBoundaryStyles
import com.github.chriskn.structurizrextension.api.view.style.styles.BoundaryStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.C4PUmlLineStyle.BOLD
import com.github.chriskn.structurizrextension.api.view.style.styles.C4PUmlLineStyle.DASHED
import com.github.chriskn.structurizrextension.api.view.style.styles.C4PUmlLineStyle.DOTTED
import com.github.chriskn.structurizrextension.internal.export.view.style.toJson
import com.structurizr.Workspace
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class BoundaryStyleExtensionTest {

    @Test
    fun `boundary style values are correctly set`() {
        val expSprite = ImageSprite(url = "img:https://plantuml.com/logo3.png", scale = 0.4)
        val expLegendSprite = OpenIconicSprite("&compass", scale = 3.0, color = "blue")
        val expTag = "styleTag"
        val expBackgroundColor = "#000000"
        val expBorder = DASHED
        val expBorderWidth = 4
        val expBorderColor = "#008000"
        val expFontColor = "#ffffff"
        val expShadowing = true
        val expC4Shape = EIGHT_SIDED
        val expLegendText = "this is a legend"

        val style = BoundaryStyle(
            tag = expTag,
            backgroundColor = expBackgroundColor,
            borderStyle = expBorder,
            borderWidth = expBorderWidth,
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
        assertThat(style.borderWidth).isEqualTo(expBorderWidth)
        assertThat(style.borderColor).isEqualTo(expBorderColor)
        assertThat(style.fontColor).isEqualTo(expFontColor)
        assertThat(style.shadowing).isEqualTo(expShadowing)
        assertThat(style.c4Shape).isEqualTo(expC4Shape)
        assertThat(style.sprite).isEqualTo(expSprite)
        assertThat(style.legendSprite).isEqualTo(expLegendSprite)
        assertThat(style.legendText).isEqualTo(expLegendText)
    }

    @Test
    fun `boundary style  can be added to ViewSet`() {
        val sprite = PlantUmlSprite(
            path = IconRegistry.iconUrlFor("postgresql")!!,
            name = "postgresql",
            scale = 0.5,
            color = "green"
        )
        val legendSprite = OpenIconicSprite("&compass")
        val style1 = BoundaryStyle(
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
        val style2 = BoundaryStyle("tag1")

        val workspace = Workspace("test", "test")
        val views = workspace.views

        views.addBoundaryStyle(style1)
        views.addBoundaryStyle(style2)

        val personStyles = views.getBoundaryStyles()
        assertThat(personStyles).hasSize(2)
        assertThat(personStyles.map { it.toJson() }).contains(style1.toJson(), style2.toJson())
    }

    @Test
    fun `boundary style  can be added to View`() {
        val style1 = BoundaryStyle(
            tag = "tag",
            backgroundColor = "#ffffff",
            borderStyle = BOLD,
            borderWidth = 5,
        )
        val style2 = BoundaryStyle("tag1")

        val workspace = Workspace("test", "test")
        val views = workspace.views

        val system = workspace.model.softwareSystem("test", "test")
        val view = views.containerView(system, "testview", "desc")
        view.addBoundaryStyle(style1)
        view.addBoundaryStyle(style2)

        val styles = view.getBoundaryStyles()
        assertThat(styles).hasSize(2)
        assertThat(styles.map { it.toJson() }).contains(style1.toJson(), style2.toJson())
    }

    @Test
    fun `IllegalArgumentException is thrown when tag is blank`() {
        assertThrows<IllegalArgumentException> {
            BoundaryStyle(" ")
        }
    }

    @Test
    fun `boundary style  can be initialized with null values`() {
        val expTag = "tag"

        val style = BoundaryStyle(tag = expTag)

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
                BoundaryStyle("test", backgroundColor = "ABC")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown for invalid font color`() {
            assertThrows<IllegalArgumentException> {
                BoundaryStyle("test", fontColor = "jellow")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown for invalid border color`() {
            assertThrows<IllegalArgumentException> {
                BoundaryStyle("test", borderColor = "")
            }
        }

        @Test
        fun `named border color is translated to hex color`() {
            val elementStyle = BoundaryStyle("test", borderColor = "green")
            assertThat(elementStyle.borderColorValidated).isEqualTo("#008000")
        }

        @Test
        fun `named font color is translated to hex color`() {
            val elementStyle = BoundaryStyle("test", fontColor = "black")
            assertThat(elementStyle.fontColorValidated).isEqualTo("#000000")
        }

        @Test
        fun `named background color is translated to hex color`() {
            val elementStyle = BoundaryStyle("test", backgroundColor = "white")
            assertThat(elementStyle.backgroundColorValidated).isEqualTo("#ffffff")
        }
    }
}
