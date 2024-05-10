package com.github.chriskn.structurizrextension.plantuml

import com.github.chriskn.structurizrextension.assertExpectedDiagramWasWrittenForView
import com.github.chriskn.structurizrextension.model.softwareSystem
import com.github.chriskn.structurizrextension.model.usedBy
import com.github.chriskn.structurizrextension.view.systemLandscapeView
import com.structurizr.Workspace
import com.structurizr.model.Model
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class DependencyLayoutTest {

    private val pathToExpectedDiagrams = "plantuml"

    private val workspace = Workspace("My Workspace", "Some Description")
    private val model: Model = workspace.model
    private val a = model.softwareSystem("A", "A")
    private val b = model.softwareSystem("B", "B")
    private val c = model.softwareSystem("C", "C")
    private val d = model.softwareSystem("D", "D")
    private val e = model.softwareSystem("E", "E")

    @BeforeAll
    fun setUpModel() {
        b.uses(a, "uses")
        c.uses(a, "uses")
        d.usedBy(a, "uses")
        e.uses(a, "uses")
    }

    @Test
    fun `position is applied`() {
        val diagramKey = "DependencyPosition"
        val contextView = workspace.views.systemLandscapeView(
            diagramKey,
            "Dependency Test",
            C4PlantUmlLayout(
                dependencyConfigurations = listOf(
                    DependencyConfiguration(filter = { it.source == b }, direction = Direction.Up),
                    DependencyConfiguration(filter = { it.source == c }, direction = Direction.Right),
                    DependencyConfiguration(filter = { it.source == a }, direction = Direction.Down),
                    DependencyConfiguration(filter = { it.source == e }, direction = Direction.Left),
                )
            )
        )
        contextView.addDefaultElements()

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
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

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
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

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }
}
