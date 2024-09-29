package com.github.chriskn.structurizrextension.internal.export.writer

import com.github.chriskn.structurizrextension.api.view.style.C4Shape
import com.github.chriskn.structurizrextension.api.view.style.borderColor
import com.github.chriskn.structurizrextension.api.view.style.c4Shape
import com.github.chriskn.structurizrextension.api.view.style.getElementStyles
import com.github.chriskn.structurizrextension.api.view.style.legendSprite
import com.github.chriskn.structurizrextension.api.view.style.legendText
import com.github.chriskn.structurizrextension.api.view.style.shadowing
import com.github.chriskn.structurizrextension.api.view.style.sprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.ImageSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.OpenIconicSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.PumlSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.Sprite
import com.github.chriskn.structurizrextension.api.view.style.technology
import com.structurizr.export.IndentingWriter
import com.structurizr.model.ModelItem
import com.structurizr.view.Border.Dashed
import com.structurizr.view.Border.Dotted
import com.structurizr.view.Border.Solid
import com.structurizr.view.ElementStyle
import com.structurizr.view.ModelView

internal object ElementStyleWriter {

    fun collectAppliedElementStyles(view: ModelView): List<ElementStyle> {
        val elements: MutableSet<ModelItem> = view.elements.map { it.element }.toMutableSet()
        val usedTags = elements.map { it.tagsAsSet }.flatten().toSet()
        val stylesForTags =
            view
                .viewSet
                .configuration
                .styles
                .elements
                .filter { usedTags.contains(it.tag) } +
                view
                    .getElementStyles()
                    .filter { usedTags.contains(it.tag) }
        return stylesForTags.distinctBy { it.tag }
    }

    fun writeElementStyle(elementStyle: ElementStyle, writer: IndentingWriter) {
        val bgColor = elementStyle.background
        val fontColor = elementStyle.color
        val borderColor = elementStyle.borderColor
        val borderStyle = when (elementStyle.border) {
            Solid -> "SolidLine()"
            Dashed -> "DashedLine()"
            Dotted -> "DottedLine()"
            else -> null
        }
        val borderThickness = elementStyle.strokeWidth?.toString()
        val shadow = elementStyle.shadowing
        val technology = elementStyle.technology
        val shapeValue = elementStyle.c4Shape
        val shape = when (shapeValue) {
            C4Shape.EIGHT_SIDED -> "EightSidedShape()"
            C4Shape.ROUNDED_BOX -> "RoundedBoxShape()"
            else -> null
        }
        val sprite = elementStyle.sprite?.toPlantUmlString()
        val legendSprite = elementStyle.legendSprite?.toPlantUmlString()
        val legendText = elementStyle.legendText
        writer.writeLine(
            """AddElementTag(${elementStyle.tag}${
                addIfNotNull("sprite", sprite)
            }${
                addIfNotNull("bgColor", bgColor)
            }${
                addIfNotNull("fontColor", fontColor)
            }${
                addIfNotNull("borderColor", borderColor)
            }${
                addIfNotNull("borderStyle", borderStyle)
            }${
                addIfNotNull("borderThickness", borderThickness)
            }${
                addIfNotNull("shadowing", shadow)
            }${
                addIfNotNull("shape", shape)
            }${
                addIfNotNull("techn", technology)
            }${
                addIfNotNull("legendSprite", legendSprite)
            }${
                addIfNotNull("legendText", legendText)
            })"""
        )
    }

    private fun addIfNotNull(name: String, value: Any?) = if (value != null) {
        """, ${'$'}$name=$value"""
    } else {
        ""
    }

    private fun Sprite.toPlantUmlString(): String = when (this) {
        is PumlSprite -> """"${spriteString(this.name, scale, validatedColor)}""""
        is OpenIconicSprite -> """"&${spriteString(this.name, scale, validatedColor)}""""
        is ImageSprite -> {
            val scaleString = scaleString(this.scale)
            if (scaleString.isBlank()) {
                """"$url""""
            } else {
                """"$url{$scaleString}""""
            }
        }

        else -> throw IllegalArgumentException("Unknown sprite type ${this::class}")
    }

    private fun spriteString(
        name: String,
        scale: Double?,
        color: String?,
    ): String {
        val scaleString = scaleString(scale)
        val colorString = colorString(color)
        return if (scaleString.isBlank() && colorString.isBlank()) {
            name
        } else if (colorString.isBlank()) {
            "$name{$scaleString}"
        } else if (scaleString.isBlank()) {
            "$name{$colorString}"
        } else {
            "$name{$scaleString,$colorString}"
        }
    }

    private fun colorString(color: String?): String = if (color != null) {
        "color=$color"
    } else {
        ""
    }

    private fun scaleString(scale: Double?): String = if (scale != null) {
        "scale=$scale"
    } else {
        ""
    }
}
