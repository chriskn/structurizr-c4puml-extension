package com.github.chriskn.structurizrextension.internal.export.writer

import com.github.chriskn.structurizrextension.api.view.style.C4Shape
import com.github.chriskn.structurizrextension.api.view.style.C4Shape.EIGHT_SIDED
import com.github.chriskn.structurizrextension.api.view.style.C4Shape.ROUNDED_BOX
import com.github.chriskn.structurizrextension.api.view.style.getBoundaryStyles
import com.github.chriskn.structurizrextension.api.view.style.getElementStyles
import com.github.chriskn.structurizrextension.api.view.style.getPersonStyles
import com.github.chriskn.structurizrextension.api.view.style.sprite.ImageSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.OpenIconicSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.PumlSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.Sprite
import com.github.chriskn.structurizrextension.api.view.style.styles.BoundaryStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.C4PumlStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.ElementStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.PersonStyle
import com.github.chriskn.structurizrextension.internal.export.view.getBoundaryContainer
import com.github.chriskn.structurizrextension.internal.export.view.getBoundaryElements
import com.github.chriskn.structurizrextension.internal.export.view.getBoundarySystems
import com.structurizr.export.IndentingWriter
import com.structurizr.model.ModelItem
import com.structurizr.model.Person
import com.structurizr.view.Border
import com.structurizr.view.Border.Dashed
import com.structurizr.view.Border.Dotted
import com.structurizr.view.Border.Solid
import com.structurizr.view.ComponentView
import com.structurizr.view.ContainerView
import com.structurizr.view.DynamicView
import com.structurizr.view.ModelView

@Suppress("TooManyFunctions")
internal object StyleWriter {

    fun collectAppliedElementStyles(view: ModelView): List<ElementStyle> {
        val elements: MutableSet<ModelItem> = view.elements.map { it.element }.toMutableSet()
        val usedTags = elements.map { it.tagsAsSet }.flatten().toSet()
        val stylesForTags = view.viewSet.getElementStyles().filter { usedTags.contains(it.tag) } +
            view.getElementStyles().filter { usedTags.contains(it.tag) }
        return stylesForTags.distinctBy { it.tag }.sortedBy { it.tag }
    }

    fun collectAppliedBoundaryStyles(view: ModelView): List<BoundaryStyle> {
        val elements: Set<ModelItem> = when (view) {
            is ContainerView -> view.getBoundarySystems().toSet()
            is ComponentView -> view.getBoundaryContainer().toSet()
            is DynamicView -> view.getBoundaryElements().toSet()
            else -> emptySet()
        }
        val usedTags = elements.map { it.tagsAsSet }.flatten().toSet()
        val stylesForTags = view.viewSet.getBoundaryStyles().filter { usedTags.contains(it.tag) } +
            view.getBoundaryStyles().filter { usedTags.contains(it.tag) }
        return stylesForTags.distinctBy { it.tag }.sortedBy { it.tag }
    }

    fun collectAppliedPersonStyles(view: ModelView): List<PersonStyle> {
        val person: MutableSet<Person> = view.elements.map { it.element }.filterIsInstance<Person>().toMutableSet()
        val usedTags = person.map { it.tagsAsSet }.flatten().toSet()
        val stylesForTags = view.viewSet.getPersonStyles().filter { usedTags.contains(it.tag) } +
            view.getPersonStyles().filter { usedTags.contains(it.tag) }
        return stylesForTags.distinctBy { it.tag }.sortedBy { it.tag }
    }

    fun writeElementStyle(elementStyle: ElementStyle, writer: IndentingWriter) {
        val bgColor = elementStyle.backgroundColor
        val fontColor = elementStyle.fontColor
        val borderColor = elementStyle.borderColor
        val borderStyle = borderStyleString(elementStyle.border)
        val borderThickness = elementStyle.borderWith?.toString()
        val shadow = elementStyle.shadowing
        val technology = elementStyle.technology
        val shapeValue = elementStyle.c4Shape
        val shape = shapeString(shapeValue)
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

    fun writeC4PumlStyle(c4PumlStyle: C4PumlStyle, writer: IndentingWriter, tagType: String) {
        val bgColor = c4PumlStyle.backgroundColor
        val fontColor = c4PumlStyle.fontColor
        val borderColor = c4PumlStyle.borderColor
        val borderStyle = borderStyleString(c4PumlStyle.border)
        val borderThickness = c4PumlStyle.borderWith?.toString()
        val shadow = c4PumlStyle.shadowing
        val shapeValue = c4PumlStyle.c4Shape
        val shape = shapeString(shapeValue)
        val sprite = c4PumlStyle.sprite?.toPlantUmlString()
        val legendSprite = c4PumlStyle.legendSprite?.toPlantUmlString()
        val legendText = c4PumlStyle.legendText
        writer.writeLine(
            """Add${tagType}Tag(${c4PumlStyle.tag}${
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
                addIfNotNull("legendSprite", legendSprite)
            }${
                addIfNotNull("legendText", legendText)
            })"""
        )
    }

    private fun borderStyleString(border: Border?): String? {
        val borderStyle = when (border) {
            Solid -> "SolidLine()"
            Dashed -> "DashedLine()"
            Dotted -> "DottedLine()"
            else -> null
        }
        return borderStyle
    }

    private fun shapeString(shapeValue: C4Shape?): String? {
        val shape = when (shapeValue) {
            EIGHT_SIDED -> "EightSidedShape()"
            ROUNDED_BOX -> "RoundedBoxShape()"
            else -> null
        }
        return shape
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
