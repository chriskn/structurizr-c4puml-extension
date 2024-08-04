package com.github.chriskn.structurizrextension.compatibility

import com.github.chriskn.structurizrextension.api.view.componentView
import com.github.chriskn.structurizrextension.api.view.containerView
import com.github.chriskn.structurizrextension.api.view.deploymentView
import com.github.chriskn.structurizrextension.api.view.dynamic.renderAsSequenceDiagram
import com.github.chriskn.structurizrextension.api.view.dynamicView
import com.github.chriskn.structurizrextension.api.view.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.api.view.layout.Layout.LeftToRight
import com.github.chriskn.structurizrextension.api.view.layout.Layout.TopDown
import com.github.chriskn.structurizrextension.api.view.layout.Legend.ShowFloatingLegend
import com.github.chriskn.structurizrextension.api.view.layout.Legend.ShowStaticLegend
import com.github.chriskn.structurizrextension.api.view.showEnterpriseBoundary
import com.github.chriskn.structurizrextension.api.view.showExternalContainerBoundaries
import com.github.chriskn.structurizrextension.api.view.showExternalSoftwareSystemBoundaries
import com.github.chriskn.structurizrextension.api.view.systemContextView
import com.github.chriskn.structurizrextension.api.view.systemLandscapeView
import com.github.chriskn.structurizrextension.assertExpectedDiagramWasWrittenForView
import com.structurizr.Workspace
import com.structurizr.model.Model
import com.structurizr.view.DynamicView
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class StructurizrCompatibilityTest {

    private val pathToExpectedDiagrams = "compatibility"

    private val workspace = Workspace("My Workspace", "Some Description")
    private val model: Model = workspace.model
    private val system1 = model.addSoftwareSystem(
        "Software System 1"
    )
    private val system2 = model.addSoftwareSystem(
        "Software System 2",
        "Description 2",
    )
    private val system3 = model.addSoftwareSystem(
        "Software System 3",
        "Description 3"
    )
    private val actor = model.addPerson("Actor")
    private val system1Container1 = system1.addContainer("Container 1 System 1")
    private val system1Container2 = system1.addContainer("Container 2 System 1", "Some description")
    private val system2Container = system2.addContainer("Container System 2", "Some container")
    private val system1Component1 = system1Container1.addComponent("Component 1 Container 1")
    private val system1Component2 = system1Container1.addComponent("Component 2 Container 1", "Some description")

    @BeforeAll
    fun setUpModel() {
        system1.addProperty("test key", "tes value")
        system1Container1.uses(system1Container2, "Container 1 uses Container 2")
        system1Component2.uses(system1Component1, "Component 2 uses Component 1")
        system2.uses(system1, "System 2 uses System 1")
        system2Container.uses(system1Container1, "System 2 Container uses System 1 Container")
        system2Container.uses(system3, "System 2 Container uses System 3")
        system1Component2.uses(system3, "System 2 Component uses System 3")
        system3.uses(system2, "System 3 uses System 2")
        actor.uses(system3, "Actor uses System 3")
        actor.uses(system1Container1, "Actor uses Container 1")
        actor.uses(system1Component2, "Actor uses Component 2")
    }

    @Test
    fun `context diagram can be exported from structurizr model`() {
        val diagramKey = "ContextView"
        val contextView = workspace.views.systemContextView(
            system1,
            diagramKey,
            "A test Context View",
            C4PlantUmlLayout(
                layout = LeftToRight,
                legend = ShowFloatingLegend,
                hideStereotypes = false
            )
        )
        contextView.addAllElements()
        // not shown because enterprise is empty (removed from structurizr API)
        contextView.showEnterpriseBoundary = true

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `landscape diagram can be exported from structurizr model`() {
        val diagramKey = "LandscapeView"
        val landscapeView = workspace.views.systemLandscapeView(
            diagramKey,
            "A test Landscape View",
            C4PlantUmlLayout(
                layout = LeftToRight,
                legend = ShowFloatingLegend,
                hideStereotypes = false
            )
        )
        landscapeView.addAllElements()
        // not shown because enterprise is empty (removed from structurizr API)
        landscapeView.showEnterpriseBoundary = true

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `container diagram can be exported from structurizr model`() {
        val diagramKey = "ContainerView"
        val containerView = workspace.views.containerView(
            system1,
            diagramKey,
            "A test Container View",
            C4PlantUmlLayout(
                layout = TopDown,
                legend = ShowStaticLegend
            )
        )
        containerView.addAllContainers()
        containerView.addAllPeople()
        containerView.showExternalSoftwareSystemBoundaries = true

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `component diagram can be exported from structurizr model`() {
        val diagramKey = "ComponentView"
        val containerView = workspace.views.componentView(
            system1Container1,
            diagramKey,
            "A test Component View",
            C4PlantUmlLayout(
                layout = TopDown,
                legend = ShowStaticLegend
            )
        )
        containerView.addAllComponents()
        containerView.addAllPeople()
        containerView.showExternalContainerBoundaries = true

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `dynamic diagram can be exported from structurizr model`() {
        val diagramKey = "DynamicView"
        createDynamicView(diagramKey)

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `sequence diagram can be exported from structurizr model`() {
        val diagramKey = "SequenceView"
        val dynamicView = createDynamicView(diagramKey)
        dynamicView.renderAsSequenceDiagram = true

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `deployment diagram can be exported from structurizr model`() {
        val cloud = model.addDeploymentNode("Cloud")
        val loadBalancer = cloud.addInfrastructureNode("Load Balancer")
        val containerRuntime = cloud.addDeploymentNode(
            "Container Runtime",
            "Something that runs containers",
            "Something"
        )
        loadBalancer.uses(containerRuntime, "forwards requests to", "something")
        containerRuntime.add(system1Container1)

        val diagramKey = "DeploymentView"
        val deploymentView = workspace.views.deploymentView(
            system1,
            diagramKey,
            "A test Deployment View"
        )
        deploymentView.addDefaultElements()

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    private fun createDynamicView(diagramKey: String): DynamicView {
        val dynamicView = workspace.views.dynamicView(
            system1,
            diagramKey,
            "A test Dynamic View"
        )
        dynamicView.add(system1Container1, "calls", system1Container2)
        dynamicView.add(system1Container2, "returns", system1Container1)
        dynamicView.add(system1Container1, "shows results", actor)

        return dynamicView
    }
}
