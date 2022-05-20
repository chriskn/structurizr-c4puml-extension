package com.github.chriskn.structurizrextension

import com.github.chriskn.structurizrextension.model.Dependency
import com.github.chriskn.structurizrextension.model.softwareSystem
import com.github.chriskn.structurizrextension.plantuml.C4PlantUmlDiagramWriter
import com.github.chriskn.structurizrextension.plantuml.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.plantuml.layout.DependencyConfiguration
import com.github.chriskn.structurizrextension.plantuml.layout.Direction
import com.github.chriskn.structurizrextension.plantuml.layout.Legend
import com.github.chriskn.structurizrextension.plantuml.layout.Mode
import com.structurizr.Workspace
import com.structurizr.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class DependencyTest {

    @TempDir
    private lateinit var tempDir: File

    private val workspace = Workspace("My Workspace", "Some Description")
    private val model: Model = workspace.model
    private val a = model.softwareSystem(
        "A",
    )
    private val b = model.softwareSystem(
        "B",
        uses = listOf(Dependency(a, "uses"))
    )
    private val c = model.softwareSystem(
        "C",
        uses = listOf(Dependency(a, "uses"))
    )
    private val d = model.softwareSystem(
        "D",
        usedBy = listOf(Dependency(a, "uses"))
    )
    private val e = model.softwareSystem(
        "E",
        uses = listOf(Dependency(a, ""))
    )

    @Test
    fun `position is applied`() {
        val diagramName = "DependencyPosition"
        val expectedDiagramContent =
            this::class.java.getResource("/expected/$diagramName.puml")!!.readText(Charsets.UTF_8)
        val contextView = workspace.views.systemLandscapeView(
            diagramName,
            "Dependency Test",
            C4PlantUmlLayout(
                dependencyConfigurations = listOf(
                    DependencyConfiguration(filter = { it.destination == a }, direction = Direction.LEFT),
                    DependencyConfiguration(filter = { it.source == a }, direction = Direction.UP)
                )
            )
        )
        contextView.addDefaultElements()

        val diagramFolder = File(tempDir, "./diagram/")
        C4PlantUmlDiagramWriter.writeDiagrams(
            diagramFolder,
            workspace
        )

        assertThat(diagramFolder.isDirectory).isTrue
        val actualDiagramFile = File(diagramFolder, "${contextView.key}.puml")
        assertThat(actualDiagramFile.isFile).isTrue
        assertThat(actualDiagramFile.readText(Charsets.UTF_8)).isEqualToIgnoringWhitespace(expectedDiagramContent)
    }

    @Test
    fun `mode is applied`() {
        val diagramName = "DependencyMode"
        val expectedDiagramContent =
            this::class.java.getResource("/expected/$diagramName.puml")!!.readText(Charsets.UTF_8)
        val contextView = workspace.views.systemLandscapeView(
            diagramName,
            "Dependency Test",
            C4PlantUmlLayout(
                dependencyConfigurations = listOf(
                    DependencyConfiguration(filter = { it.source == a }, mode = Mode.NEIGHBOR)
                )
            )
        )
        contextView.addDefaultElements()

        val diagramFolder = File(tempDir, "./diagram/")
        C4PlantUmlDiagramWriter.writeDiagrams(
            diagramFolder,
            workspace
        )

        assertThat(diagramFolder.isDirectory).isTrue
        val actualDiagramFile = File(diagramFolder, "${contextView.key}.puml")
        assertThat(actualDiagramFile.isFile).isTrue
        assertThat(actualDiagramFile.readText(Charsets.UTF_8)).isEqualToIgnoringWhitespace(expectedDiagramContent)
    }

    @Test
    fun `position is ignored when mode is overridden`() {
        val diagramName = "DependencyPositionMode"
        val expectedDiagramContent =
            this::class.java.getResource("/expected/$diagramName.puml")!!.readText(Charsets.UTF_8)
        val contextView = workspace.views.systemLandscapeView(
            diagramName,
            "Dependency Test",
            C4PlantUmlLayout(
                dependencyConfigurations = listOf(
                    DependencyConfiguration(filter = { it.source == a }, mode = Mode.BACK, direction = Direction.LEFT)
                ),
                legend = Legend.NONE
            )
        )
        contextView.addDefaultElements()

        val diagramFolder = File(tempDir, "./diagram/")
        C4PlantUmlDiagramWriter.writeDiagrams(
            diagramFolder,
            workspace
        )

        assertThat(diagramFolder.isDirectory).isTrue
        val actualDiagramFile = File(diagramFolder, "${contextView.key}.puml")
        assertThat(actualDiagramFile.isFile).isTrue
        assertThat(actualDiagramFile.readText(Charsets.UTF_8)).isEqualToIgnoringWhitespace(expectedDiagramContent)
    }
}
