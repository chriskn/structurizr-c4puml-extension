package com.github.chriskn.structurizrextension.view.style.boundary

import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.api.model.softwareSystem
import com.github.chriskn.structurizrextension.api.view.containerView
import com.github.chriskn.structurizrextension.api.view.style.C4Shape.EIGHT_SIDED
import com.github.chriskn.structurizrextension.api.view.style.C4Shape.ROUNDED_BOX
import com.github.chriskn.structurizrextension.api.view.style.addBoundaryStyle
import com.github.chriskn.structurizrextension.api.view.style.getBoundaryStyles
import com.github.chriskn.structurizrextension.api.view.style.sprite.ImageSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.OpenIconicSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.PumlSprite
import com.github.chriskn.structurizrextension.api.view.style.styles.createBoundaryStyle
import com.github.chriskn.structurizrextension.internal.export.view.style.toJson
import com.structurizr.Workspace
import com.structurizr.view.Border.Dashed
import com.structurizr.view.Border.Dotted
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class BoundaryStyleExtensionTest {

    @Test
    fun `boundary style  values are correctly set`() {
        val expSprite = ImageSprite("img:https://plantuml.com/logo3.png", 0.4)
        val expLegendSprite = OpenIconicSprite("compass", scale = 3.0, color = "blue")
        val expTag = "styleTag"
        val expBackgroundColor = "#000000"
        val expBorder = Dashed
        val expBorderWith = 4
        val expBorderColor = "#008000"
        val expFontColor = "#ffffff"
        val expShadowing = true
        val expC4Shape = EIGHT_SIDED
        val expLegendText = "this is a legend"

        val style = createBoundaryStyle(
            tag = expTag,
            backgroundColor = expBackgroundColor,
            border = expBorder,
            borderWith = expBorderWith,
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
        assertThat(style.border).isEqualTo(expBorder)
        assertThat(style.borderWith).isEqualTo(expBorderWith)
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
        val sprite = PumlSprite(
            includeUrl = IconRegistry.iconUrlFor("postgresql")!!,
            name = "postgresql",
            scale = 0.5,
            color = "green"
        )
        val legendSprite = OpenIconicSprite("compass")
        val style1 = createBoundaryStyle(
            tag = "tag",
            backgroundColor = "#ffffff",
            border = Dotted,
            borderWith = 5,
            borderColor = "purple",
            fontColor = "red",
            shadowing = false,
            c4Shape = ROUNDED_BOX,
            sprite = sprite,
            legendSprite = legendSprite,
            legendText = "this is a legend text"
        )
        val style2 = createBoundaryStyle("tag1")

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
        val style1 = createBoundaryStyle(
            tag = "tag",
            backgroundColor = "#ffffff",
            border = Dotted,
            borderWith = 5,
        )
        val style2 = createBoundaryStyle("tag1")

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
            createBoundaryStyle(" ")
        }
    }

    @Test
    fun `boundary style  can be initialized with null values`() {
        val expTag = "tag"

        val style = createBoundaryStyle(tag = expTag)

        assertThat(style.tag).isEqualTo(expTag)
        assertThat(style.backgroundColor).isNull()
        assertThat(style.border).isNull()
        assertThat(style.borderWith).isNull()
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
                createBoundaryStyle("test", backgroundColor = "ABC")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown for invalid font color`() {
            assertThrows<IllegalArgumentException> {
                createBoundaryStyle("test", fontColor = "jellow")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown for invalid border color`() {
            assertThrows<IllegalArgumentException> {
                createBoundaryStyle("test", borderColor = "")
            }
        }

        @Test
        fun `named border color is translated to hex color`() {
            val elementStyle = createBoundaryStyle("test", borderColor = "green")
            assertThat(elementStyle.borderColor).isEqualTo("#008000")
        }

        @Test
        fun `named font color is translated to hex color`() {
            val elementStyle = createBoundaryStyle("test", fontColor = "black")
            assertThat(elementStyle.fontColor).isEqualTo("#000000")
        }

        @Test
        fun `named background color is translated to hex color`() {
            val elementStyle = createBoundaryStyle("test", backgroundColor = "white")
            assertThat(elementStyle.backgroundColor).isEqualTo("#ffffff")
        }
    }
}
