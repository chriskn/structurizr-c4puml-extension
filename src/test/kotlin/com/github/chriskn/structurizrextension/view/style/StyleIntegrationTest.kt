package com.github.chriskn.structurizrextension.view.style

import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.api.model.Dependency
import com.github.chriskn.structurizrextension.api.model.component
import com.github.chriskn.structurizrextension.api.model.container
import com.github.chriskn.structurizrextension.api.model.deploymentNode
import com.github.chriskn.structurizrextension.api.model.infrastructureNode
import com.github.chriskn.structurizrextension.api.model.person
import com.github.chriskn.structurizrextension.api.model.softwareSystem
import com.github.chriskn.structurizrextension.api.view.componentView
import com.github.chriskn.structurizrextension.api.view.containerView
import com.github.chriskn.structurizrextension.api.view.deploymentView
import com.github.chriskn.structurizrextension.api.view.dynamic.add
import com.github.chriskn.structurizrextension.api.view.dynamic.renderAsSequenceDiagram
import com.github.chriskn.structurizrextension.api.view.dynamicView
import com.github.chriskn.structurizrextension.api.view.showExternalBoundaries
import com.github.chriskn.structurizrextension.api.view.showExternalContainerBoundaries
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
import com.github.chriskn.structurizrextension.api.view.style.styles.createBoundaryStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.createElementStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.createPersonStyle
import com.github.chriskn.structurizrextension.api.view.systemContextView
import com.github.chriskn.structurizrextension.assertExpectedDiagramWasWrittenForView
import com.structurizr.Workspace
import com.structurizr.view.Border.Dashed
import com.structurizr.view.Border.Dotted
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class StyleIntegrationTest {

    private val pathToExpectedDiagrams = "view/style"

    private val workspace = Workspace("My Workspace", "")
    private val model = workspace.model
    private val views = workspace.views

    private val systemTag = "System Style"
    private val boundaryTag = "Boundary Style"
    private val containerTag = "Container Style"
    private val componentTag = "Component Style"
    private val personTag = "Person Style"

    private val system =
        model.softwareSystem("My Software System", "system", tags = listOf(systemTag, boundaryTag))
    private val container =
        system.container("My Container", "container", tags = listOf(containerTag, boundaryTag))
    private val component = container.component("My Component", "component", tags = listOf(componentTag))
    private val person = model.person(
        name = "Person",
        tags = listOf(personTag),
        uses = listOf(
            Dependency(system, "uses system"),
            Dependency(container, "uses container"),
            Dependency(component, "uses component"),
        )
    )

    private val sprite = ImageSprite("img:https://plantuml.com/logo3.png", 0.4)
    private val legendSprite = OpenIconicSprite("compass", scale = 3.0)
    private val systemStyle = createElementStyle(
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
    private val personStyle = createPersonStyle(
        tag = personTag,
        backgroundColor = "#00FF00",
        border = Dashed,
        borderWith = 4,
        borderColor = "red",
        fontColor = "blue",
        c4Shape = EIGHT_SIDED,
        legendText = "this is a person"
    )
    private val containerStyle = createElementStyle(
        tag = containerTag,
        backgroundColor = "#ffffff",
        border = Dotted,
        borderWith = 5,
        borderColor = "purple",
        fontColor = "red",
        technology = "REST",
        c4Shape = ROUNDED_BOX,
        sprite = PumlSprite(
            includeUrl = IconRegistry.iconUrlFor("postgresql")!!,
            name = "postgresql",
            scale = 0.5,
            color = "green"
        ),
        legendSprite = OpenIconicSprite("compass"),
        legendText = "this is a legend container"
    )
    private val boundaryStyle = createBoundaryStyle(
        tag = boundaryTag,
        backgroundColor = "#00FFFF",
        border = Dotted,
        borderWith = 4,
        borderColor = "red",
        fontColor = "green",
        legendText = "this is a system"
    )
    private val componentStyle = createElementStyle(
        tag = componentTag,
        backgroundColor = "#ffffff",
        border = Dotted,
        borderWith = 5,
        borderColor = "purple",
        fontColor = "red",
        shadowing = false,
        technology = "REST",
        c4Shape = ROUNDED_BOX,
        sprite = OpenIconicSprite("compass"),
        legendSprite = OpenIconicSprite("compass"),
        legendText = "this is a legend text"
    )

    @BeforeEach
    fun resetStyles() {
        views.clearElementStyles()
    }

    @Test
    fun `styles are applied to system context view`() {
        val diagramKey = "SystemStyleTest"

        views.addElementStyle(systemStyle)
        views.addPersonStyle(personStyle)

        val view = workspace.views.systemContextView(system, diagramKey, "SystemStyleTest")
        view.addAllPeople()

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `styles are applied to container view`() {
        val diagramKey = "ContainerStyleTest"
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
    fun `styles are applied to component view`() {
        val diagramKey = "ComponentStyleTest"
        views.addElementStyle(componentStyle)
        views.addPersonStyle(personStyle)
        views.addBoundaryStyle(boundaryStyle)

        val componentView = workspace.views.componentView(container, diagramKey, "ComponentStyleTest")
        componentView.addAllComponents()
        componentView.addAllPeople()
        componentView.showExternalContainerBoundaries = true

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `styles are applied to dynamic view`() {
        val diagramKey = "DynamicStyleTest"
        views.addElementStyle(containerStyle)
        views.addPersonStyle(personStyle)
        views.addBoundaryStyle(boundaryStyle)

        val dynamicView = workspace.views.dynamicView(system, diagramKey, diagramKey)
        dynamicView.add(person, container, "uses")
        dynamicView.showExternalBoundaries = true

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `styles are applied to dynamic sequence`() {
        val diagramKey = "DynamicStyleSequenceTest"
        views.addElementStyle(containerStyle)
        views.addPersonStyle(personStyle)
        views.addBoundaryStyle(boundaryStyle)

        val dynamicView = workspace.views.dynamicView(system, diagramKey, diagramKey)
        dynamicView.add(person, container, "uses")
        dynamicView.showExternalBoundaries = true
        dynamicView.renderAsSequenceDiagram = true

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `styles are applied to deployment view`() {
        val deploymentNodeTag = "Deployment Node Tag"
        val infrastructureNodeTag = "Infrastructure Node Tag"

        val deploymentNodeStyle = createElementStyle(
            tag = deploymentNodeTag,
            backgroundColor = "#00EEFF",
            borderWith = 4,
        )

        val infrastructureNodeStyle = createElementStyle(
            tag = infrastructureNodeTag,
            backgroundColor = "#bcaadd",
            borderColor = "green",
            borderWith = 3,
        )

        val aws = model.deploymentNode(
            name = "AWS",
            description = "Production AWS environment",
            icon = "aws",
        )
        val nodeWithStyle = aws.deploymentNode(
            name = "Node with style",
            hostsContainers = listOf(container),
            tags = listOf(deploymentNodeTag),
        )
        val nodeWithoutStyle = aws.deploymentNode(
            name = "Node without style",
            hostsContainers = listOf(system.container("Some Container", ""))
        )
        aws.infrastructureNode(
            name = "Some Infrastructure Node",
            description = "",
            tags = listOf(infrastructureNodeTag),
            usedBy = listOf(
                Dependency(nodeWithoutStyle, "uses"),
                Dependency(nodeWithStyle, "uses")
            ),
        )

        val diagramKey = "DeploymentStyleTest"
        views.addElementStyle(containerStyle)
        views.addElementStyle(deploymentNodeStyle)
        views.addElementStyle(infrastructureNodeStyle)

        val deploymentView = workspace.views.deploymentView(system, diagramKey, diagramKey)
        deploymentView.addAllDeploymentNodes()

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `styles can be applied for single views`() {
        val diagramKeyWithStyle = "ViewStyleTestWithStyle"
        val diagramKeyWithoutStyle = "ViewStyleTestWithoutStyle"

        val containerViewWithStyle = workspace.views.containerView(
            system,
            diagramKeyWithStyle,
            "ViewStyleTestWithStyle"
        )
        containerViewWithStyle.add(container)
        containerViewWithStyle.addAllPeople()
        containerViewWithStyle.showExternalSoftwareSystemBoundaries = true
        containerViewWithStyle.addElementStyle(containerStyle.copy(legendText = "this is a container"))
        containerViewWithStyle.addPersonStyle(personStyle.copy(legendText = "this is a legend"))
        containerViewWithStyle.addBoundaryStyle(boundaryStyle)

        val containerViewWithoutStyle = workspace.views.containerView(
            system,
            diagramKeyWithoutStyle,
            "ViewStyleTestWithoutStyle"
        )
        containerViewWithoutStyle.add(container)

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
