package com.github.chriskn.structurizrextension.view.layout

import com.github.chriskn.structurizrextension.api.model.softwareSystem
import com.github.chriskn.structurizrextension.api.model.usedBy
import com.github.chriskn.structurizrextension.api.view.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.api.view.layout.DependencyConfiguration
import com.github.chriskn.structurizrextension.api.view.layout.Direction.Down
import com.github.chriskn.structurizrextension.api.view.layout.Direction.Left
import com.github.chriskn.structurizrextension.api.view.layout.Direction.Right
import com.github.chriskn.structurizrextension.api.view.layout.Direction.Up
import com.github.chriskn.structurizrextension.api.view.layout.Legend.None
import com.github.chriskn.structurizrextension.api.view.layout.Legend.ShowStaticLegend
import com.github.chriskn.structurizrextension.api.view.layout.Mode.Back
import com.github.chriskn.structurizrextension.api.view.layout.Mode.Neighbor
import com.github.chriskn.structurizrextension.api.view.systemLandscapeView
import com.github.chriskn.structurizrextension.assertExpectedDiagramWasWrittenForView
import com.structurizr.Workspace
import com.structurizr.model.Model
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class DependencyLayoutTest {

    private val pathToExpectedDiagrams = "dependency"

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
                    DependencyConfiguration(filter = { it.source == b }, direction = Up),
                    DependencyConfiguration(filter = { it.source == c }, direction = Right),
                    DependencyConfiguration(filter = { it.source == a }, direction = Down),
                    DependencyConfiguration(filter = { it.source == e }, direction = Left),
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
                    DependencyConfiguration(filter = { it.source == a }, mode = Neighbor)
                ),
                legend = ShowStaticLegend
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
                    DependencyConfiguration(filter = { it.source == a }, mode = Back, direction = Left)
                ),
                legend = None
            )
        )
        contextView.addDefaultElements()

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }
}
