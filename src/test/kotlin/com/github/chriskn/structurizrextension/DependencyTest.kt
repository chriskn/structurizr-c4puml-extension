package com.github.chriskn.structurizrextension

import com.github.chriskn.structurizrextension.model.Dependency
import com.github.chriskn.structurizrextension.model.softwareSystem
import com.github.chriskn.structurizrextension.plantuml.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.plantuml.layout.DependencyConfiguration
import com.github.chriskn.structurizrextension.plantuml.layout.Direction
import com.github.chriskn.structurizrextension.plantuml.layout.Legend
import com.github.chriskn.structurizrextension.plantuml.layout.Mode
import com.structurizr.Workspace
import com.structurizr.model.Model
import org.junit.jupiter.api.Test

class DependencyTest {

    private val workspace = Workspace("My Workspace", "Some Description")
    private val model: Model = workspace.model
    private val a = model.softwareSystem(
        "A", "A"
    )
    private val b = model.softwareSystem("B", "B", uses = listOf(Dependency(a, "uses")))
    private val c = model.softwareSystem("C", "C", uses = listOf(Dependency(a, "uses")))
    private val d = model.softwareSystem("D", "C", usedBy = listOf(Dependency(a, "uses")))
    private val e = model.softwareSystem("E", "E", uses = listOf(Dependency(a, "")))

    @Test
    fun `position is applied`() {
        val diagramKey = "DependencyPosition"
        val expectedDiagramContent =
            this::class.java.getResource("/expected/$diagramKey.puml")!!.readText(Charsets.UTF_8)
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

        assertExpectedDiagramWasWrittenForView(workspace, diagramKey, expectedDiagramContent)
    }

    @Test
    fun `mode is applied`() {
        val diagramKey = "DependencyMode"
        val expectedDiagramContent =
            this::class.java.getResource("/expected/$diagramKey.puml")!!.readText(Charsets.UTF_8)
        val contextView = workspace.views.systemLandscapeView(
            diagramKey,
            "Dependency Test",
            C4PlantUmlLayout(
                dependencyConfigurations = listOf(
                    DependencyConfiguration(filter = { it.source == a }, mode = Mode.Neighbor)
                )
            )
        )
        contextView.addDefaultElements()

        assertExpectedDiagramWasWrittenForView(workspace, diagramKey, expectedDiagramContent)
    }

    @Test
    fun `position is ignored when mode is overridden`() {
        val diagramKey = "DependencyPositionMode"
        val expectedDiagramContent =
            this::class.java.getResource("/expected/$diagramKey.puml")!!.readText(Charsets.UTF_8)
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

        assertExpectedDiagramWasWrittenForView(workspace, diagramKey, expectedDiagramContent)
    }
}
