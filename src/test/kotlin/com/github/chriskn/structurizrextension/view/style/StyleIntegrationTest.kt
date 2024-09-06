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
import com.github.chriskn.structurizrextension.api.view.style.C4PUmlElementShape.EIGHT_SIDED
import com.github.chriskn.structurizrextension.api.view.style.C4PUmlElementShape.ROUNDED_BOX
import com.github.chriskn.structurizrextension.api.view.style.addBoundaryStyle
import com.github.chriskn.structurizrextension.api.view.style.addDependencyStyle
import com.github.chriskn.structurizrextension.api.view.style.addElementStyle
import com.github.chriskn.structurizrextension.api.view.style.addPersonStyle
import com.github.chriskn.structurizrextension.api.view.style.sprite.ImageSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.OpenIconicSprite
import com.github.chriskn.structurizrextension.api.view.style.sprite.PUmlSprite
import com.github.chriskn.structurizrextension.api.view.style.styles.BoundaryStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.C4PUmlLineStyle.BOLD
import com.github.chriskn.structurizrextension.api.view.style.styles.C4PUmlLineStyle.DASHED
import com.github.chriskn.structurizrextension.api.view.style.styles.C4PUmlLineStyle.DOTTED
import com.github.chriskn.structurizrextension.api.view.style.styles.DependencyStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.ElementStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.createPersonStyle
import com.github.chriskn.structurizrextension.api.view.systemContextView
import com.github.chriskn.structurizrextension.assertExpectedDiagramWasWrittenForView
import com.structurizr.Workspace
import com.structurizr.view.clearBoundaryStyles
import com.structurizr.view.clearDependencyStyles
import com.structurizr.view.clearElementStyles
import com.structurizr.view.clearPersonStyles
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
    private val dependencyTag = "Dependency Style"

    private val system =
        model.softwareSystem("My Software System", "system", tags = listOf(systemTag, boundaryTag))
    private val container =
        system.container("My Container", "container", tags = listOf(containerTag, boundaryTag))
    private val component = container.component("My Component", "component", tags = listOf(componentTag))
    private val person = model.person(
        name = "Person",
        tags = listOf(personTag),
        uses = listOf(
            Dependency(system, "uses system", tags = listOf(dependencyTag)),
            Dependency(container, "uses container", tags = listOf(dependencyTag)),
            Dependency(component, "uses component", tags = listOf(dependencyTag)),
        )
    )

    private val sprite = ImageSprite("img:https://plantuml.com/logo3.png", 0.4)
    private val legendSprite = OpenIconicSprite("compass", scale = 3.0)
    private val systemStyle = ElementStyle(
        tag = systemTag,
        backgroundColor = "#000000",
        borderStyle = DASHED,
        borderWidth = 4,
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
        sprite = PUmlSprite(
            url = IconRegistry.iconUrlFor("apple")!!,
            name = IconRegistry.iconFileNameFor("apple")!!,
            scale = 0.5,
            color = "green"
        ),
        backgroundColor = "#00FF00",
        borderStyle = DASHED,
        borderWidth = 4,
        borderColor = "red",
        fontColor = "blue",
        c4Shape = EIGHT_SIDED,
        legendText = "this is a apple"
    )
    private val containerStyle = ElementStyle(
        tag = containerTag,
        backgroundColor = "#ffffff",
        borderStyle = DOTTED,
        borderWidth = 5,
        borderColor = "purple",
        fontColor = "red",
        technology = "REST",
        c4Shape = ROUNDED_BOX,
        sprite = PUmlSprite(
            url = IconRegistry.iconUrlFor("postgresql")!!,
            name = IconRegistry.iconFileNameFor("postgresql")!!,
            scale = 0.5,
            color = "green"
        ),
        legendSprite = OpenIconicSprite("compass"),
        legendText = "this is a legend container"
    )
    private val boundaryStyle = BoundaryStyle(
        tag = boundaryTag,
        backgroundColor = "#00FFFF",
        borderStyle = DOTTED,
        borderWidth = 4,
        borderColor = "red",
        fontColor = "green",
        legendText = "this is a system"
    )
    private val componentStyle = ElementStyle(
        tag = componentTag,
        backgroundColor = "#ffffff",
        borderStyle = BOLD,
        borderWidth = 5,
        borderColor = "purple",
        fontColor = "red",
        shadowing = false,
        technology = "REST",
        c4Shape = ROUNDED_BOX,
        sprite = OpenIconicSprite("compass"),
        legendSprite = OpenIconicSprite("compass"),
        legendText = "this is a legend text"
    )
    private val androidSprite = PUmlSprite(
        name = IconRegistry.iconFileNameFor("android")!!,
        url = IconRegistry.iconUrlFor("android")!!,
        color = "green",
        scale = 1.3
    )
    private val dependencyStyle = DependencyStyle(
        tag = dependencyTag,
        fontColor = "#aa9999",
        sprite = androidSprite,
        legendText = "Android user uses",
        legendSprite = androidSprite.copy(scale = 0.3),
        technology = "Android",
        lineColor = "green",
        lineStyle = DOTTED,
        lineWidth = 2
    )

    @BeforeEach
    fun resetStyles() {
        views.configuration.clearElementStyles()
        views.configuration.clearBoundaryStyles()
        views.configuration.clearPersonStyles()
        views.configuration.clearDependencyStyles()
    }

    @Test
    fun `styles are applied to system context view`() {
        val diagramKey = "SystemStyleTest"

        views.addElementStyle(systemStyle)
        views.addPersonStyle(personStyle)
        views.addDependencyStyle(dependencyStyle)

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
        views.addDependencyStyle(dependencyStyle)

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
        views.addDependencyStyle(dependencyStyle)

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
        views.addDependencyStyle(dependencyStyle)

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
        views.addDependencyStyle(dependencyStyle)

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

        val deploymentNodeStyle = ElementStyle(
            tag = deploymentNodeTag,
            backgroundColor = "#00EEFF",
            borderWidth = 4,
        )

        val infrastructureNodeStyle = ElementStyle(
            tag = infrastructureNodeTag,
            backgroundColor = "#bcaadd",
            borderColor = "green",
            borderWidth = 3,
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
                Dependency(nodeWithoutStyle, "uses", tags = listOf(dependencyTag)),
                Dependency(nodeWithStyle, "uses")
            ),
        )

        val diagramKey = "DeploymentStyleTest"
        views.addElementStyle(containerStyle)
        views.addElementStyle(deploymentNodeStyle)
        views.addElementStyle(infrastructureNodeStyle)
        views.addDependencyStyle(dependencyStyle)

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
        containerViewWithStyle.addDependencyStyle(dependencyStyle)

        val containerViewWithoutStyle = workspace.views.containerView(
            system,
            diagramKeyWithoutStyle,
            "ViewStyleTestWithoutStyle"
        )
        containerViewWithoutStyle.add(container)
        containerViewWithStyle.showExternalSoftwareSystemBoundaries = true
        containerViewWithoutStyle.addAllPeople()

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKeyWithStyle)
        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKeyWithoutStyle)
    }

    @Test
    fun `styles for unused tags are not exported`() {
        val diagramKey = "ViewStyleTestUnusedTag"

        val unusedElementStyle = ElementStyle(
            tag = "someUnusedTag1",
            backgroundColor = "#ffffff",
            legendText = "this is a legend text"
        )
        val unusedPersonStyle = createPersonStyle(
            tag = "someUnusedTag2",
            backgroundColor = "#ffffff",
            legendText = "this is a legend text"
        )
        val unusedBoundaryStyle = BoundaryStyle(
            tag = "someUnusedTag3",
            backgroundColor = "#ffffff",
            legendText = "this is a legend text"
        )
        val unusedDependencyStyle = DependencyStyle(
            tag = "someUnusedTag4",
            lineColor = "#ffffff",
            legendText = "this is a legend text"
        )
        val containerViewWithStyle =
            workspace.views.containerView(system, diagramKey, "ViewStyleTestWithStyle")
        containerViewWithStyle.addAllContainers()
        containerViewWithStyle.addElementStyle(unusedElementStyle)
        containerViewWithStyle.addPersonStyle(unusedPersonStyle)
        containerViewWithStyle.addBoundaryStyle(unusedBoundaryStyle)
        containerViewWithStyle.addDependencyStyle(unusedDependencyStyle)

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }
}
