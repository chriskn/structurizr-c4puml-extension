package com.github.chriskn.structurizrextension.internal.export.writer

import com.github.chriskn.structurizrextension.api.view.style.getBoundaryStyles
import com.github.chriskn.structurizrextension.api.view.style.getDependencyStyles
import com.github.chriskn.structurizrextension.api.view.style.getElementStyles
import com.github.chriskn.structurizrextension.api.view.style.getPersonStyles
import com.github.chriskn.structurizrextension.api.view.style.sprite.ImageSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.OpenIconicSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.PUmlSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.Sprite
import com.github.chriskn.structurizrextension.api.view.style.styles.BoundaryStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.C4PUmlLineStyle.DASHED
import com.github.chriskn.structurizrextension.api.view.style.styles.DependencyStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.ElementStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.ModelElementStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.PersonStyle
import com.github.chriskn.structurizrextension.internal.export.view.getBoundaryContainer
import com.github.chriskn.structurizrextension.internal.export.view.getBoundaryElements
import com.github.chriskn.structurizrextension.internal.export.view.getBoundarySystems
import com.structurizr.export.IndentingWriter
import com.structurizr.model.InteractionStyle
import com.structurizr.model.ModelItem
import com.structurizr.model.Person
import com.structurizr.view.ComponentView
import com.structurizr.view.ContainerView
import com.structurizr.view.DynamicView
import com.structurizr.view.ModelView

@Suppress("TooManyFunctions")
internal object StyleWriter {

    private const val ASYNC_REL_TAG_NAME = "async relationship"

    private val asyncDependencyStyle = DependencyStyle(
        tag = ASYNC_REL_TAG_NAME,
        lineStyle = DASHED,
        legendText = "async relationship (dashed)",
    )

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
        val person = view.elements.map { it.element }.filterIsInstance<Person>().toSet()
        val usedTags = person.map { it.tagsAsSet }.flatten().toSet()
        val stylesForTags = view.viewSet.getPersonStyles().filter { usedTags.contains(it.tag) } +
            view.getPersonStyles().filter { usedTags.contains(it.tag) }
        return stylesForTags.distinctBy { it.tag }.sortedBy { it.tag }
    }

    fun collectAppliedDependencyStyles(view: ModelView): List<DependencyStyle> {
        val usedTags = view.relationships.map { it.relationship.tagsAsSet }.flatten()
        val stylesForTags = (
            view.viewSet.getDependencyStyles().filter { usedTags.contains(it.tag) } +
                view.getDependencyStyles().filter { usedTags.contains(it.tag) }
            ).toMutableList()
        if (view.relationships.any { it.relationship.interactionStyle == InteractionStyle.Asynchronous }) {
            stylesForTags.add(asyncDependencyStyle)
        }
        return stylesForTags.distinctBy { it.tag }.sortedBy { it.tag }
    }

    fun writeElementStyle(elementStyle: ElementStyle, writer: IndentingWriter) {
        val bgColor = elementStyle.backgroundColorValidated
        val fontColor = elementStyle.fontColorValidated
        val borderColor = elementStyle.borderColorValidated
        val borderStyle = elementStyle.borderStyle?.pUmlString
        val borderThickness = elementStyle.borderWidth?.toString()
        val shadow = elementStyle.shadowing
        val technology = elementStyle.technology
        val shape = elementStyle.c4Shape?.pUmlString
        val sprite = elementStyle.sprite?.toPlantUmlString()
        val legendSprite = elementStyle.legendSprite?.toPlantUmlString()
        val legendText = elementStyle.legendText
        writer.writeLine(
            """AddElementTag("${elementStyle.tag}"${
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

    fun writeModelElementStyle(modelElementStyle: ModelElementStyle, writer: IndentingWriter, tagType: String) {
        val bgColor = modelElementStyle.backgroundColorValidated
        val fontColor = modelElementStyle.fontColorValidated
        val borderColor = modelElementStyle.borderColorValidated
        val borderStyle = modelElementStyle.borderStyle?.pUmlString
        val borderThickness = modelElementStyle.borderWidth?.toString()
        val shadow = modelElementStyle.shadowing
        val shape = modelElementStyle.c4Shape?.pUmlString
        val sprite = modelElementStyle.sprite?.toPlantUmlString()
        val legendSprite = modelElementStyle.legendSprite?.toPlantUmlString()
        val legendText = modelElementStyle.legendText
        writer.writeLine(
            """Add${tagType}Tag("${modelElementStyle.tag}"${
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

    fun writeDependencyStyle(dependencyStyle: DependencyStyle, writer: IndentingWriter) {
        val fontColor = dependencyStyle.fontColorValidated
        val sprite = dependencyStyle.sprite?.toPlantUmlString()
        val legendSprite = dependencyStyle.legendSprite?.toPlantUmlString()
        val legendText = dependencyStyle.legendText
        val technology = dependencyStyle.technology
        val lineColor = dependencyStyle.lineColorValidated
        val lineStyle = dependencyStyle.lineStyle?.pUmlString
        val lineWidth = dependencyStyle.lineWidth
        writer.writeLine(
            """AddRelTag("${dependencyStyle.tag}"${
                addIfNotNull("sprite", sprite)
            }${
                addIfNotNull("lineColor", lineColor)
            }${
                addIfNotNull("textColor", fontColor)
            }${
                addIfNotNull("techn", technology)
            }${
                addIfNotNull("lineStyle", lineStyle)
            }${
                addIfNotNull("lineThickness", lineWidth)
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

    internal fun Sprite.toPlantUmlString(): String = when (this) {
        is PUmlSprite -> """"${spriteString(this.name, scale, colorValidated)}""""
        is OpenIconicSprite -> """"&${spriteString(this.name, scale, colorValidated)}""""
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
