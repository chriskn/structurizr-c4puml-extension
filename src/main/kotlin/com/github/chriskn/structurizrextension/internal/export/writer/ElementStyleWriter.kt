package com.github.chriskn.structurizrextension.internal.export.writer

import com.github.chriskn.structurizrextension.api.view.style.BORDER_COLOR_PROPERTY_NAME
import com.github.chriskn.structurizrextension.api.view.style.LEGEND_SPRITE_PROPERTY_NAME
import com.github.chriskn.structurizrextension.api.view.style.LEGEND_TEXT_PROPERTY_NAME
import com.github.chriskn.structurizrextension.api.view.style.SHADOWING_PROPERTY_NAME
import com.github.chriskn.structurizrextension.api.view.style.SHAPE_PROPERTY_NAME
import com.github.chriskn.structurizrextension.api.view.style.SPRITE_PROPERTY_NAME
import com.github.chriskn.structurizrextension.api.view.style.Shape
import com.github.chriskn.structurizrextension.api.view.style.TECHNOLOGY_PROPERTY_NAME
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
        val borderColor = elementStyle.properties[BORDER_COLOR_PROPERTY_NAME]
        val borderStyle = when (elementStyle.border) {
            Solid -> "SolidLine()"
            Dashed -> "DashedLine()"
            Dotted -> "DottedLine()"
            else -> null
        }
        val borderThickness = elementStyle.strokeWidth?.toString()
        val shadow = elementStyle.properties[SHADOWING_PROPERTY_NAME]
        val technology = elementStyle.properties[TECHNOLOGY_PROPERTY_NAME]
        val shapeValue = elementStyle.properties[SHAPE_PROPERTY_NAME]?.let { Shape.valueOf(it) }
        val shape = when (shapeValue) {
            Shape.EIGHT_SIDED -> "EightSidedShape()"
            Shape.ROUNDED_BOX -> "RoundedBoxShape()"
            else -> null
        }
        val sprite = elementStyle.properties[SPRITE_PROPERTY_NAME]
        val legendSprite = elementStyle.properties[LEGEND_SPRITE_PROPERTY_NAME]
        val legendText = elementStyle.properties[LEGEND_TEXT_PROPERTY_NAME]
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
