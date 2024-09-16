package com.github.chriskn.structurizrextension.internal.export.writer

import com.github.chriskn.structurizrextension.api.icons.AWS_ICON_COMMONS
import com.github.chriskn.structurizrextension.api.icons.AWS_ICON_URL
import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.api.model.icon
import com.github.chriskn.structurizrextension.api.view.dynamic.renderAsSequenceDiagram
import com.github.chriskn.structurizrextension.api.view.layout.LayoutRegistry
import com.structurizr.export.IndentingWriter
import com.structurizr.export.plantuml.C4PlantUMLExporter.C4PLANTUML_SHADOW
import com.structurizr.export.plantuml.C4PlantUMLExporter.C4PLANTUML_SPRITE
import com.structurizr.model.DeploymentNode
import com.structurizr.model.InteractionStyle
import com.structurizr.model.ModelItem
import com.structurizr.util.StringUtils
import com.structurizr.view.*
import java.net.URI
import java.net.URL
import javax.imageio.IIOException
import javax.imageio.ImageIO
import kotlin.math.max


internal object HeaderWriter {

    private const val ASYNC_REL_TAG_NAME = "async relationship"
    private const val C4_PLANT_UML_STDLIB_URL =
        "https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master"
    private const val MAX_ICON_SIZE: Double = 30.0


    private val includes = mutableSetOf<URI>()

    fun writeHeader(view: ModelView, writer: IndentingWriter) {
        includes.clear()
        addIncludeUrls(view)
        // Spaces in PlantUML ids can cause issues. Alternatively, id can be surrounded with double quotes
        writer.writeLine("@startuml(id=${view.key.replace(' ', '_')})")
        for (include in includes) {
            writer.writeLine("!includeurl $include")
        }
        var viewTitle = view.title
        if (viewTitle.isNullOrBlank()) {
            viewTitle = view.name
        }
        writer.writeLine("title $viewTitle")
        if (!view.description.isNullOrBlank()) {
            writer.writeLine("caption " + view.description)
            writer.writeLine()
        }
        val layout = LayoutRegistry.layoutForKey(view.key)
        if (layout.showPersonOutline) {
            writer.writeLine("SHOW_PERSON_OUTLINE()")
        }
        // sequence diagrams do not support layout
        if (!(view is DynamicView && view.renderAsSequenceDiagram)) {
            writer.writeLine(layout.layout.macro)
        }
        writer.writeLine()
        if (view.relationships.any { it.relationship.interactionStyle == InteractionStyle.Asynchronous }) {
            writeAsyncRelTag(writer)
        }
        val elementStyles = view.elements.map { view.viewSet.configuration.styles.findElementStyle(it.element) }
            .associateBy { it.tag }
        for (entry in elementStyles.entries) {
            val elementStyle = entry.value
            val tagList = entry.key.replaceFirst("Element,", "")
            if (tagList.isNotEmpty()) {
                var sprite: String? = ""
                if (elementStyleHasSupportedIcon(elementStyle)) {
                    val scale: Double = calculateIconScale(elementStyle.icon)
                    sprite = ("img:" + elementStyle.icon).toString() + "{scale=" + scale + "}"
                }
                sprite = elementStyle.properties.getOrDefault(C4PLANTUML_SPRITE, sprite)

                var borderThickness = 1
                if (elementStyle.strokeWidth != null) {
                    borderThickness = elementStyle.strokeWidth
                }

                var line = java.lang.String.format(
                    "AddElementTag(\"%s\", \$bgColor=\"%s\", \$borderColor=\"%s\", \$fontColor=\"%s\", \$sprite=\"%s\", \$shadowing=\"%s\", \$borderStyle=\"%s\", \$borderThickness=\"%s\")",
                    tagList,
                    elementStyle.background,
                    elementStyle.stroke,
                    elementStyle.color,
                    sprite,
                    elementStyle.properties.getOrDefault(C4PLANTUML_SHADOW, ""),
                    elementStyle.border.toString().toLowerCase(),
                    borderThickness
                )

                line = line.replace(", \$borderThickness=\"1\")", ")")
                writer.writeLine(line)
            }
        }
        val relationshipStyles = view.relationships
            .map { view.viewSet.configuration.styles.findRelationshipStyle(it.relationship) }
            .associateBy { it.tag }

        for (entry in relationshipStyles.entries) {
            val relationshipStyle = entry.value
            val tagList = entry.key.replaceFirst("Relationship,", "")

            var lineStyle = "\"\""
            if (relationshipStyle!!.style == LineStyle.Dashed) {
                lineStyle = "DashedLine()"
            } else if (relationshipStyle.style == LineStyle.Dotted) {
                lineStyle = "DottedLine()"
            }

            writer.writeLine(
                String.format(
                    "AddRelTag(\"%s\", \$textColor=\"%s\", \$lineColor=\"%s\", \$lineStyle = %s)",
                    tagList,
                    relationshipStyle.color,
                    relationshipStyle.color,
                    lineStyle
                )
            )
        }

        // TODO boundary styles
    }

    private fun elementStyleHasSupportedIcon(elementStyle: ElementStyle): Boolean {
        return !StringUtils.isNullOrEmpty(elementStyle.icon) && elementStyle.icon.startsWith("http")
    }

    private fun calculateIconScale(iconUrl: String?): Double {
        var scale = 0.5

        try {
            val url = URL(iconUrl)
            val bi = ImageIO.read(url)

            val width = bi.width
            val height = bi.height

            scale = MAX_ICON_SIZE / max(width.toDouble(), height.toDouble())
        } catch (e: UnsupportedOperationException) {
            // This is a known issue on native builds since AWT packages aren't available.
            // So we just swallow the error and use the default scale
        } catch (e: UnsatisfiedLinkError) {
        } catch (e: IIOException) {
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return scale
    }

    private fun addIncludeUrls(view: View) {
        val elements: MutableSet<ModelItem> = (view as ModelView).elements.map { it.element }.toMutableSet()
        if (view is DeploymentView) {
            val children = elements
                .filterIsInstance<DeploymentNode>()
                .flatMap { collectElements(it, elements) }
            elements.addAll(children)
        }
        elements += view.relationships.map { it.relationship }
        val spriteIncludesForElements = elements
            .asSequence()
            .mapNotNull { it.icon?.let { technology -> IconRegistry.iconUrlFor(technology) } }
            .toSet()
            .toList()
            .sorted()
            .toMutableList()
        if (spriteIncludesForElements.any { it.startsWith(AWS_ICON_URL) }) {
            spriteIncludesForElements.add(0, AWS_ICON_COMMONS)
        }
        spriteIncludesForElements.forEach { includes.add(URI(it)) }
        val c4PumlIncludeURI = URI("$C4_PLANT_UML_STDLIB_URL/${includeForView(view)}")
        includes.add(c4PumlIncludeURI)
    }

    @Suppress("MaxLineLength")
    private fun writeAsyncRelTag(writer: IndentingWriter) {
        writer.writeLine(
            """AddRelTag("$ASYNC_REL_TAG_NAME", ${'$'}textColor="${'$'}ARROW_COLOR", ${'$'}lineColor="${'$'}ARROW_COLOR", ${'$'}lineStyle = DashedLine())"""
        )
        writer.writeLine()
    }

    private fun collectElements(
        deploymentNode: DeploymentNode,
        elements: MutableSet<ModelItem>
    ): MutableSet<ModelItem> {
        elements.addAll(deploymentNode.infrastructureNodes)
        elements.addAll(deploymentNode.softwareSystemInstances.map { it.softwareSystem })
        elements.addAll(deploymentNode.containerInstances.map { it.container })
        deploymentNode.children.forEach { collectElements(it, elements) }
        return elements
    }

    private fun includeForView(view: View) = when (view) {
        is DynamicView -> {
            if (view.renderAsSequenceDiagram) {
                "C4_Sequence.puml"
            } else {
                "C4_Dynamic.puml"
            }
        }

        is DeploymentView -> "C4_Deployment.puml"
        is ComponentView -> "C4_Component.puml"
        is ContainerView -> "C4_Container.puml"
        is SystemLandscapeView -> "C4_Context.puml"
        is SystemContextView -> "C4_Context.puml"
        else -> throw IllegalArgumentException("Unsupported view of class ${view::class.java.simpleName}")
    }
}
