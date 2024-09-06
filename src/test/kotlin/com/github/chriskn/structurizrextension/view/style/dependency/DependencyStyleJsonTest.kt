package com.github.chriskn.structurizrextension.view.style.dependency

import com.github.chriskn.structurizrextension.api.view.style.sprite.ImageSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.OpenIconicSprite
import com.github.chriskn.structurizrextension.api.view.style.styles.C4PUmlLineStyle.DASHED
import com.github.chriskn.structurizrextension.api.view.style.styles.DependencyStyle
import com.github.chriskn.structurizrextension.internal.export.view.style.dependencyStyleFromJson
import com.github.chriskn.structurizrextension.internal.export.view.style.toJson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DependencyStyleJsonTest {

    @Test
    fun `dependency style is serialized and deserialized correctly`() {
        val expSprite = ImageSprite("img:https://plantuml.com/logo3.png", 0.4)
        val expLegendSprite = OpenIconicSprite("compass", scale = 3.0, color = "blue")
        val expTag = "styleTag"
        val expBackgroundColor = "#000000"
        val expBorder = DASHED
        val expBorderWith = 4
        val expFontColor = "#ffffff"
        val expTechnology = "Kafka"
        val expLegendText = "this is a legend"

        val style = DependencyStyle(
            tag = expTag,
            lineColor = expBackgroundColor,
            lineStyle = expBorder,
            lineWidth = expBorderWith,
            fontColor = expFontColor,
            technology = expTechnology,
            sprite = expSprite,
            legendSprite = expLegendSprite,
            legendText = expLegendText
        )

        val styleJson = style.toJson()
        val deserializedStyle = dependencyStyleFromJson(styleJson)

        assertThat(deserializedStyle.tag).isEqualTo(expTag)
        assertThat(deserializedStyle.lineColor).isEqualTo(expBackgroundColor)
        assertThat(deserializedStyle.lineStyle).isEqualTo(expBorder)
        assertThat(deserializedStyle.lineWidth).isEqualTo(expBorderWith)
        assertThat(deserializedStyle.fontColor).isEqualTo(expFontColor)
        assertThat(deserializedStyle.technology).isEqualTo(expTechnology)
        assertThat(deserializedStyle.sprite).isEqualTo(expSprite)
        assertThat(deserializedStyle.legendSprite).isEqualTo(expLegendSprite)
        assertThat(deserializedStyle.legendText).isEqualTo(expLegendText)
    }
}
