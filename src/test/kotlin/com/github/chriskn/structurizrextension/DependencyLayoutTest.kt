package com.github.chriskn.structurizrextension

import com.github.chriskn.structurizrextension.model.Dependency
import com.github.chriskn.structurizrextension.model.softwareSystem
import com.github.chriskn.structurizrextension.plantuml.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.plantuml.DependencyConfiguration
import com.github.chriskn.structurizrextension.plantuml.Direction
import com.github.chriskn.structurizrextension.plantuml.Legend
import com.github.chriskn.structurizrextension.plantuml.Mode
import com.github.chriskn.structurizrextension.view.systemLandscapeView
import com.structurizr.Workspace
import com.structurizr.model.Model
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DependencyLayoutTest {

    private val workspace = Workspace("My Workspace", "Some Description")
    private val model: Model = workspace.model
    private val a = model.softwareSystem("A", "A")

    @BeforeEach
    fun setUpModel() {
        model.softwareSystem("B", "B", uses = listOf(Dependency(a, "uses")))
        model.softwareSystem("C", "C", uses = listOf(Dependency(a, "uses")))
        model.softwareSystem("D", "C", usedBy = listOf(Dependency(a, "uses")))
        model.softwareSystem("E", "E", uses = listOf(Dependency(a, "")))
    }

    @Test
    fun `position is applied`() {
        val diagramKey = "DependencyPosition"
        val contextView = workspace.views.systemLandscapeView(
            diagramKey,
            "Dependency Test",
            C4PlantUmlLayout(
                dependencyConfigurations = listOf(
                    DependencyConfiguration(filter = { it.destination == a }, direction = Direction.Left),
                    DependencyConfiguration(filter = { it.source == a }, direction = Direction.Up)
                )
            )
        )
        contextView.addDefaultElements()

        assertExpectedDiagramWasWrittenForView(workspace, diagramKey)
    }

    @Test
    fun `mode is applied`() {
        val diagramKey = "DependencyMode"
        val contextView = workspace.views.systemLandscapeView(
            diagramKey,
            "Dependency Test",
            C4PlantUmlLayout(
                dependencyConfigurations = listOf(
                    DependencyConfiguration(filter = { it.source == a }, mode = Mode.Neighbor)
                ),
                legend = Legend.ShowStaticLegend
            )
        )
        contextView.addDefaultElements()

        assertExpectedDiagramWasWrittenForView(workspace, diagramKey)
    }

    @Test
    fun `position is ignored when mode is overridden`() {
        val diagramKey = "DependencyPositionMode"
        val contextView = workspace.views.systemLandscapeView(
            diagramKey,
            "Dependency Test",
            C4PlantUmlLayout(
                dependencyConfigurations = listOf(
                    DependencyConfiguration(filter = { it.source == a }, mode = Mode.Back, direction = Direction.Left)
                ),
                legend = Legend.None
            )
        )
        contextView.addDefaultElements()

        assertExpectedDiagramWasWrittenForView(workspace, diagramKey)
    }
}
