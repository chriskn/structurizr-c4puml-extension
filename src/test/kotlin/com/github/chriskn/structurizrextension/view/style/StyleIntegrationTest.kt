package com.github.chriskn.structurizrextension.view.style

import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.api.model.Dependency
import com.github.chriskn.structurizrextension.api.model.component
import com.github.chriskn.structurizrextension.api.model.container
import com.github.chriskn.structurizrextension.api.model.person
import com.github.chriskn.structurizrextension.api.model.softwareSystem
import com.github.chriskn.structurizrextension.api.view.componentView
import com.github.chriskn.structurizrextension.api.view.containerView
import com.github.chriskn.structurizrextension.api.view.showExternalSoftwareSystemBoundaries
import com.github.chriskn.structurizrextension.api.view.style.C4Shape.EIGHT_SIDED
import com.github.chriskn.structurizrextension.api.view.style.C4Shape.ROUNDED_BOX
import com.github.chriskn.structurizrextension.api.view.style.addBoundaryStyle
import com.github.chriskn.structurizrextension.api.view.style.addElementStyle
import com.github.chriskn.structurizrextension.api.view.style.addPersonStyle
import com.github.chriskn.structurizrextension.api.view.style.clearElementStyles
import com.github.chriskn.structurizrextension.api.view.style.sprite.ImageSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.OpenIconicSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.PumlSprite
import com.github.chriskn.structurizrextension.api.view.style.styles.BoundaryStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.ElementStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.PersonStyle
import com.github.chriskn.structurizrextension.api.view.systemContextView
import com.github.chriskn.structurizrextension.assertExpectedDiagramWasWrittenForView
import com.structurizr.Workspace
import com.structurizr.view.Border.Dashed
import com.structurizr.view.Border.Dotted
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class StyleIntegrationTest {

    private val pathToExpectedDiagrams = "view/style/element"

    private val workspace = Workspace("My Workspace", "")
    private val model = workspace.model
    private val views = workspace.views

    private val systemTag = "System Style"
    private val systemBoundaryTag = "System Boundary Style"
    private val containerTag = "Container Style"
    private val containerBoundaryTag = "Container Boundary Style"
    private val componentTag = "Component Style"
    private val personTag = "Person Style"

    private val system =
        model.softwareSystem("My Software System", "system", tags = listOf(systemTag, systemBoundaryTag))
    private val container =
        system.container("My Container", "container", tags = listOf(containerTag, containerBoundaryTag))
    private val component = container.component("My Component", "component", tags = listOf(componentTag))

    @BeforeAll
    fun setUpModel() {
        model.person(
            name = "Person",
            tags = listOf(personTag),
            uses = listOf(
                Dependency(system, "uses system"),
                Dependency(container, "uses container"),
                Dependency(component, "uses component"),
            )
        )
    }

    @BeforeEach
    fun resetStyles() {
        views.clearElementStyles()
    }

    @Test
    fun `styles are applied to system context diagram`() {
        val diagramKey = "SystemStyleTest"
        val sprite = ImageSprite("img:https://plantuml.com/logo3.png", 0.4)
        val legendSprite = OpenIconicSprite("compass", scale = 3.0)
        val systemStyle = ElementStyle(
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
        val personStyle = PersonStyle(
            tag = personTag,
            backgroundColor = "#00FF00",
            border = Dashed,
            borderWith = 4,
            borderColor = "red",
            fontColor = "blue",
            c4Shape = EIGHT_SIDED,
            legendText = "this is a legend"
        )
        views.addElementStyle(systemStyle)
        views.addPersonStyle(personStyle)

        val view = workspace.views.systemContextView(system, diagramKey, "SystemStyleTest")
        view.addAllPeople()

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `element styles are applied to container`() {
        val diagramKey = "ContainerStyleTest"
        val sprite = PumlSprite(
            includeUrl = IconRegistry.iconUrlFor("postgresql")!!,
            name = "postgresql",
            scale = 0.5,
            color = "green"
        )
        val legendSprite = OpenIconicSprite("compass")
        val containerStyle = ElementStyle(
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
            legendText = "this is a legend container"
        )
        val personStyle = PersonStyle(
            tag = personTag,
            backgroundColor = "#00FF00",
            border = Dashed,
            borderWith = 4,
            borderColor = "red",
            fontColor = "blue",
            c4Shape = EIGHT_SIDED,
            legendText = "this is a person"
        )
        val boundaryStyle = BoundaryStyle(
            tag = systemBoundaryTag,
            backgroundColor = "#00FFFF",
            border = Dotted,
            borderWith = 4,
            borderColor = "red",
            fontColor = "green",
            legendText = "this is a system"
        )
        views.addElementStyle(containerStyle)
        views.addPersonStyle(personStyle)
        views.addBoundaryStyle(boundaryStyle)

        val containerView = workspace.views.containerView(system, diagramKey, "ContainerStyleTest")
        containerView.addAllContainers()
        containerView.addAllPeople()
        containerView.showExternalSoftwareSystemBoundaries = true

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `element styles are applied to component`() {
        val diagramKey = "ComponentStyleTest"
        val sprite = OpenIconicSprite("compass")
        val componentStyle = ElementStyle(
            tag = componentTag,
            backgroundColor = "#ffffff",
            border = Dotted,
            borderWith = 5,
            borderColor = "purple",
            fontColor = "red",
            shadowing = false,
            technology = "REST",
            c4Shape = ROUNDED_BOX,
            sprite = sprite,
            legendSprite = sprite,
            legendText = "this is a legend text"
        )
        val boundaryStyle = BoundaryStyle(
            tag = systemBoundaryTag,
            backgroundColor = "#00FFFF",
            border = Dotted,
            borderWith = 4,
            borderColor = "red",
            fontColor = "green",
            legendText = "this is a system"
        )
        val personStyle = PersonStyle(
            tag = personTag,
            backgroundColor = "#00FF00",
            border = Dashed,
            borderWith = 4,
            borderColor = "red",
            fontColor = "blue",
            c4Shape = EIGHT_SIDED,
            legendText = "this is a person"
        )
        views.addElementStyle(componentStyle)
        views.addPersonStyle(personStyle)
        views.addBoundaryStyle(boundaryStyle)

        val componentView = workspace.views.componentView(container, diagramKey, "ComponentStyleTest")
        componentView.addAllComponents()

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
        val containerTag = ElementStyle(
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

        val unusedStyle = ElementStyle(
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
