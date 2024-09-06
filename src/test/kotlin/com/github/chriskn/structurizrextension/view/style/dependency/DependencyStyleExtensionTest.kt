package com.github.chriskn.structurizrextension.view.style.dependency

import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.api.model.softwareSystem
import com.github.chriskn.structurizrextension.api.view.containerView
import com.github.chriskn.structurizrextension.api.view.style.addDependencyStyle
import com.github.chriskn.structurizrextension.api.view.style.getDependencyStyles
import com.github.chriskn.structurizrextension.api.view.style.sprite.ImageSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.OpenIconicSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.PUmlSprite
import com.github.chriskn.structurizrextension.api.view.style.styles.C4PUmlLineStyle.BOLD
import com.github.chriskn.structurizrextension.api.view.style.styles.C4PUmlLineStyle.DASHED
import com.github.chriskn.structurizrextension.api.view.style.styles.C4PUmlLineStyle.DOTTED
import com.github.chriskn.structurizrextension.api.view.style.styles.DependencyStyle
import com.github.chriskn.structurizrextension.internal.export.view.style.toJson
import com.structurizr.Workspace
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DependencyStyleExtensionTest {

    @Test
    fun `dependency style values are correctly set`() {
        val expSprite = ImageSprite("img:https://plantuml.com/logo3.png", 0.4)
        val expLegendSprite = OpenIconicSprite("compass", scale = 3.0, color = "blue")
        val expTag = "styleTag"
        val expLineColor = "#00ff00"
        val expLineStyle = DASHED
        val expLineWidth = 4
        val expFontColor = "#ffffff"
        val expTechnology = "Kafka"
        val expLegendText = "this is a legend"

        val style = DependencyStyle(
            tag = expTag,
            lineColor = expLineColor,
            lineStyle = expLineStyle,
            lineWidth = expLineWidth,
            fontColor = expFontColor,
            technology = expTechnology,
            sprite = expSprite,
            legendSprite = expLegendSprite,
            legendText = expLegendText,
        )

        assertThat(style.tag).isEqualTo(expTag)
        assertThat(style.lineStyle).isEqualTo(expLineStyle)
        assertThat(style.lineWidth).isEqualTo(expLineWidth)
        assertThat(style.lineColor).isEqualTo(expLineColor)
        assertThat(style.fontColor).isEqualTo(expFontColor)
        assertThat(style.technology).isEqualTo(expTechnology)
        assertThat(style.sprite).isEqualTo(expSprite)
        assertThat(style.legendSprite).isEqualTo(expLegendSprite)
        assertThat(style.legendText).isEqualTo(expLegendText)
    }

    @Test
    fun `dependency style can be added to ViewSet`() {
        val sprite = PUmlSprite(
            url = IconRegistry.iconUrlFor("postgresql")!!,
            name = "postgresql",
            scale = 0.5,
            color = "green"
        )
        val legendSprite = OpenIconicSprite("compass")
        val style1 = DependencyStyle(
            tag = "tag",
            lineColor = "#ffffff",
            lineStyle = DOTTED,
            lineWidth = 5,
            fontColor = "red",
            technology = "REST",
            sprite = sprite,
            legendSprite = legendSprite,
            legendText = "this is a legend text"
        )
        val style2 = DependencyStyle("tag1")

        val workspace = Workspace("test", "test")
        val views = workspace.views

        views.addDependencyStyle(style1)
        views.addDependencyStyle(style2)

        val dependencyStyles = views.getDependencyStyles()
        assertThat(dependencyStyles).hasSize(2)
        assertThat(dependencyStyles.firstOrNull { it.tag == style1.tag }).isEqualTo(style1)
        assertThat(dependencyStyles.firstOrNull { it.tag == style2.tag }).isEqualTo(style2)
    }

    @Test
    fun `dependency style can be added to View`() {
        val style1 = DependencyStyle(
            tag = "tag",
            lineColor = "#ffffff",
            lineStyle = BOLD,
            lineWidth = 5,
        )
        val style2 = DependencyStyle("tag1")

        val workspace = Workspace("test", "test")
        val views = workspace.views

        val system = workspace.model.softwareSystem("test", "test")
        val view = views.containerView(system, "testview", "desc")
        view.addDependencyStyle(style1)
        view.addDependencyStyle(style2)

        val elementStyles = view.getDependencyStyles()
        assertThat(elementStyles).hasSize(2)
        assertThat(elementStyles.map { it.toJson() }).contains(style1.toJson(), style2.toJson())
    }

    @Test
    fun `IllegalArgumentException is thrown when tag is blank`() {
        assertThrows<IllegalArgumentException> {
            DependencyStyle(" ")
        }
    }

    @Test
    fun `dependency style can be initialized with null values`() {
        val expTag = "tag"

        val style = DependencyStyle(tag = expTag)

        assertThat(style.tag).isEqualTo(expTag)
        assertThat(style.lineColor).isNull()
        assertThat(style.lineStyle).isNull()
        assertThat(style.lineWidth).isNull()
        assertThat(style.fontColor).isNull()
        assertThat(style.technology).isNull()
        assertThat(style.sprite).isNull()
        assertThat(style.legendSprite).isNull()
        assertThat(style.legendText).isNull()
    }

    @Nested
    inner class Color {

        @Test
        fun `IllegalArgumentException is thrown for invalid line color`() {
            assertThrows<IllegalArgumentException> {
                DependencyStyle("test", lineColor = "ABC")
            }
        }

        @Test
        fun `IllegalArgumentException is thrown for invalid font color`() {
            assertThrows<IllegalArgumentException> {
                DependencyStyle("test", fontColor = "jellow")
            }
        }

        @Test
        fun `named line color is translated to hex color`() {
            val elementStyle = DependencyStyle("test", lineColor = "green")
            assertThat(elementStyle.lineColorValidated).isEqualTo("#008000")
        }

        @Test
        fun `named font color is translated to hex color`() {
            val elementStyle = DependencyStyle("test", fontColor = "black")
            assertThat(elementStyle.fontColorValidated).isEqualTo("#000000")
        }
    }
}
