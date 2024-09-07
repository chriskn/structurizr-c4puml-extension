package com.github.chriskn.structurizrextension.internal.export.writer

import com.github.chriskn.structurizrextension.api.view.style.C4Shape
import com.github.chriskn.structurizrextension.api.view.style.borderColor
import com.github.chriskn.structurizrextension.api.view.style.c4Shape
import com.github.chriskn.structurizrextension.api.view.style.legendSprite
import com.github.chriskn.structurizrextension.api.view.style.legendText
import com.github.chriskn.structurizrextension.api.view.style.shadowing
import com.github.chriskn.structurizrextension.api.view.style.sprite
import com.github.chriskn.structurizrextension.api.view.style.technology
import com.structurizr.export.IndentingWriter
import com.structurizr.model.ModelItem
import com.structurizr.view.Border.Dashed
import com.structurizr.view.Border.Dotted
import com.structurizr.view.Border.Solid
import com.structurizr.view.ElementStyle
import com.structurizr.view.ModelView

internal object ElementStyleWriter {

    fun collectElementStyles(view: ModelView): List<ElementStyle> {
        val elements: MutableSet<ModelItem> = view.elements.map { it.element }.toMutableSet()
        val usedTags = elements.map { it.tagsAsSet }.flatten().toSet()
        val stylesForTags = view.viewSet.configuration.styles.elements.filter { usedTags.contains(it.tag) }
        return stylesForTags
    }

    fun writeElementStyle(elementStyle: ElementStyle, writer: IndentingWriter) {
        // AddElementTag(, ?techn, ?legendText, )
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
        val sprite = elementStyle.sprite
        val legendSprite = elementStyle.legendSprite
        val legendText = elementStyle.legendText
        writer.writeLine(
            """AddElementTag(${elementStyle.tag}${
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
                addIfNotNull("sprite", sprite)
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
}
