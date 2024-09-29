package com.github.chriskn.structurizrextension.view.style

import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.api.model.component
import com.github.chriskn.structurizrextension.api.model.container
import com.github.chriskn.structurizrextension.api.model.softwareSystem
import com.github.chriskn.structurizrextension.api.view.containerView
import com.github.chriskn.structurizrextension.api.view.style.C4Shape.EIGHT_SIDED
import com.github.chriskn.structurizrextension.api.view.style.C4Shape.ROUNDED_BOX
import com.github.chriskn.structurizrextension.api.view.style.addElementStyle
import com.github.chriskn.structurizrextension.api.view.style.clearElementStyles
import com.github.chriskn.structurizrextension.api.view.style.createElementStyle
import com.github.chriskn.structurizrextension.api.view.style.sprite.ImageSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.OpenIconicSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.PumlSprite
import com.github.chriskn.structurizrextension.api.view.systemContextView
import com.github.chriskn.structurizrextension.assertExpectedDiagramWasWrittenForView
import com.structurizr.Workspace
import com.structurizr.view.Border.Dashed
import com.structurizr.view.Border.Dotted
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ElementStyleIntegrationTest {

    private val pathToExpectedDiagrams = "view/style"

    private val workspace = Workspace("My Workspace", "")
    private val model = workspace.model
    private val views = workspace.views

    private val systemTag = "System Style"
    private val containerTag = "Container Style"
    private val componentTag = "Component Style"

    private val system = model.softwareSystem("My Software System", "system", tags = listOf(systemTag))
    private val container = system.container("My Container", "container", tags = listOf(containerTag))
    private val component = container.component("My Component", "component", tags = listOf(componentTag))

    @BeforeEach
    fun resetStyles() {
        views.clearElementStyles()
    }

    @Test
    fun `element style is applied for software systems`() {
        val diagramKey = "SystemStyleTest"

        workspace.views.systemContextView(system, diagramKey, "SystemStyleTest")

        val sprite = ImageSprite("img:https://plantuml.com/logo3.png", 0.4)
        val legendSprite = OpenIconicSprite("compass", scale = 3.0)
        val systemStyle = createElementStyle(
            tag = systemTag,
            backgroundColor = "#000000",
            border = Dashed,
            borderWith = 4,
            borderColor = "green",
            fontColor = "yellow",
            shadowing = true,
            technology = "Kafka",
            c4Shape = EIGHT_SIDED,
            sprite = sprite,
            legendSprite = legendSprite,
            legendText = "this is a legend"
        )
        views.addElementStyle(systemStyle)

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `element style is applied for container`() {
        val diagramKey = "ContainerStyleTest"

        val containerView = workspace.views.containerView(system, diagramKey, "ContainerStyleTest")
        containerView.addAllContainers()

        val sprite = PumlSprite(
            includeUrl = IconRegistry.iconUrlFor("postgresql")!!,
            name = "postgresql",
            scale = 0.5,
            color = "green"
        )
        val legendSprite = OpenIconicSprite("compass")
        val containerStyle = createElementStyle(
            tag = containerTag,
            backgroundColor = "#ffffff",
            border = Dotted,
            borderWith = 5,
            borderColor = "purple",
            fontColor = "red",
            shadowing = false,
            technology = "REST",
            c4Shape = ROUNDED_BOX,
            sprite = sprite,
            legendSprite = legendSprite,
            legendText = "this is a legend text"
        )
        views.addElementStyle(containerStyle)

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `element style can be applied for single views`() {
        val diagramKeyWithStyle = "ViewStyleTestWithStyle"
        val diagramKeyWithoutStyle = "ViewStyleTestWithoutStyle"

        val sprite = PumlSprite(
            includeUrl = IconRegistry.iconUrlFor("postgresql")!!,
            name = "postgresql",
            scale = 0.5,
            color = "green"
        )
        val legendSprite = OpenIconicSprite("compass")
        val containerTag = createElementStyle(
            tag = containerTag,
            backgroundColor = "#ffffff",
            border = Dotted,
            borderWith = 5,
            borderColor = "purple",
            fontColor = "red",
            shadowing = false,
            technology = "REST",
            c4Shape = ROUNDED_BOX,
            sprite = sprite,
            legendSprite = legendSprite,
            legendText = "this is a legend text"
        )
        val containerViewWithStyle =
            workspace.views.containerView(system, diagramKeyWithStyle, "ViewStyleTestWithStyle")
        containerViewWithStyle.addAllContainers()
        containerViewWithStyle.addElementStyle(containerTag)
        val containerViewWithoutStyle =
            workspace.views.containerView(system, diagramKeyWithoutStyle, "ViewStyleTestWithoutStyle")
        containerViewWithoutStyle.addAllContainers()

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKeyWithStyle)
        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKeyWithoutStyle)
    }

    @Test
    fun `element style for unused tags are not exported`() {
        val diagramKey = "ViewStyleTestUnusedTag"

        val unusedStyle = createElementStyle(
            tag = "someUnusedTag",
            backgroundColor = "#ffffff",
            legendText = "this is a legend text"
        )
        val containerViewWithStyle =
            workspace.views.containerView(system, diagramKey, "ViewStyleTestWithStyle")
        containerViewWithStyle.addAllContainers()
        containerViewWithStyle.addElementStyle(unusedStyle)

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }
}
