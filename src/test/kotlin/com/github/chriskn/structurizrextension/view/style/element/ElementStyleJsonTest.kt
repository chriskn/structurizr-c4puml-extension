package com.github.chriskn.structurizrextension.view.style.element

import com.github.chriskn.structurizrextension.api.view.sprite.sprites.ImageSprite
import com.github.chriskn.structurizrextension.api.view.sprite.sprites.OpenIconicSprite
import com.github.chriskn.structurizrextension.api.view.style.C4PUmlElementShape.EIGHT_SIDED
import com.github.chriskn.structurizrextension.api.view.style.styles.C4PUmlLineStyle.DASHED
import com.github.chriskn.structurizrextension.api.view.style.styles.ElementStyle
import com.github.chriskn.structurizrextension.internal.export.view.style.elementStyleFromJson
import com.github.chriskn.structurizrextension.internal.export.view.style.toJson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ElementStyleJsonTest {

    @Test
    fun `element style is serialized and deserialized correctly`() {
        val expSprite = ImageSprite("img:https://plantuml.com/logo3.png", 0.4)
        val expLegendSprite = OpenIconicSprite("&compass", scale = 3.0, color = "blue")
        val expTag = "styleTag"
        val expBackgroundColor = "#000000"
        val expBorder = DASHED
        val expBorderWith = 4
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
            borderWidth = expBorderWith,
            borderColor = expBorderColor,
            fontColor = expFontColor,
            shadowing = expShadowing,
            technology = expTechnology,
            c4Shape = expC4Shape,
            sprite = expSprite,
            legendSprite = expLegendSprite,
            legendText = expLegendText
        )

        val styleJson = style.toJson()
        val deserializedStyle = elementStyleFromJson(styleJson)

        assertThat(deserializedStyle.tag).isEqualTo(expTag)
        assertThat(deserializedStyle.backgroundColor).isEqualTo(expBackgroundColor)
        assertThat(deserializedStyle.borderStyle).isEqualTo(expBorder)
        assertThat(deserializedStyle.borderWidth).isEqualTo(expBorderWith)
        assertThat(deserializedStyle.borderColor).isEqualTo(expBorderColor)
        assertThat(deserializedStyle.fontColor).isEqualTo(expFontColor)
        assertThat(deserializedStyle.shadowing).isEqualTo(expShadowing)
        assertThat(deserializedStyle.technology).isEqualTo(expTechnology)
        assertThat(deserializedStyle.c4Shape).isEqualTo(expC4Shape)
        assertThat(deserializedStyle.sprite).isEqualTo(expSprite)
        assertThat(deserializedStyle.legendSprite).isEqualTo(expLegendSprite)
        assertThat(deserializedStyle.legendText).isEqualTo(expLegendText)
    }
}
