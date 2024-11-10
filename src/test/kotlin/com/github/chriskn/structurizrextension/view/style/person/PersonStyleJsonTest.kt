package com.github.chriskn.structurizrextension.view.style.person

import com.github.chriskn.structurizrextension.api.view.sprite.ImageSprite
import com.github.chriskn.structurizrextension.api.view.sprite.OpenIconicSprite
import com.github.chriskn.structurizrextension.api.view.style.C4PUmlElementShape.EIGHT_SIDED
import com.github.chriskn.structurizrextension.api.view.style.styles.C4PUmlLineStyle.SOLID
import com.github.chriskn.structurizrextension.api.view.style.styles.createPersonStyle
import com.github.chriskn.structurizrextension.internal.export.view.style.elementStyleFromJson
import com.github.chriskn.structurizrextension.internal.export.view.style.toJson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PersonStyleJsonTest {

    @Test
    fun `person is serialized and deserialized correctly`() {
        val expSprite = ImageSprite("img:https://plantuml.com/logo3.png", 0.4)
        val expLegendSprite = OpenIconicSprite("&compass", scale = 3.0, color = "blue")
        val expTag = "styleTag"
        val expBackgroundColor = "#000000"
        val expBorder = SOLID
        val expBorderWidth = 4
        val expBorderColor = "#008000"
        val expFontColor = "#ffffff"
        val expShadowing = true
        val expC4Shape = EIGHT_SIDED
        val expLegendText = "this is a legend"

        val style = createPersonStyle(
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

        val styleJson = style.toJson()
        val deserializedStyle = elementStyleFromJson(styleJson)

        assertThat(deserializedStyle.tag).isEqualTo(expTag)
        assertThat(deserializedStyle.backgroundColor).isEqualTo(expBackgroundColor)
        assertThat(deserializedStyle.borderStyle).isEqualTo(expBorder)
        assertThat(deserializedStyle.borderWidth).isEqualTo(expBorderWidth)
        assertThat(deserializedStyle.borderColor).isEqualTo(expBorderColor)
        assertThat(deserializedStyle.fontColor).isEqualTo(expFontColor)
        assertThat(deserializedStyle.shadowing).isEqualTo(expShadowing)
        assertThat(deserializedStyle.c4Shape).isEqualTo(expC4Shape)
        assertThat(deserializedStyle.sprite).isEqualTo(expSprite)
        assertThat(deserializedStyle.legendSprite).isEqualTo(expLegendSprite)
        assertThat(deserializedStyle.legendText).isEqualTo(expLegendText)
    }
}
