package com.github.chriskn.structurizrextension

import com.github.chriskn.structurizrextension.model.C4Properties
import com.github.chriskn.structurizrextension.model.Dependency
import com.github.chriskn.structurizrextension.model.person
import com.github.chriskn.structurizrextension.model.softwareSystem
import com.github.chriskn.structurizrextension.plantuml.C4PlantUmlDiagramWriter
import com.github.chriskn.structurizrextension.plantuml.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.plantuml.layout.Layout
import com.github.chriskn.structurizrextension.plantuml.layout.Legend
import com.structurizr.Workspace
import com.structurizr.model.Enterprise
import com.structurizr.model.Location
import com.structurizr.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class ContextViewTest {

    @TempDir
    private lateinit var tempDir: File

    private val workspace = Workspace("My Workspace", "Some Description")
    private val model: Model = workspace.model
    private val system0 = model.softwareSystem(
        "Software System 0",
        "Description 0",
        Location.External,
        tags = listOf("tag1", "tag2")
    )
    private val system1 = model.softwareSystem(
        name = "Software System 1",
        icon = "android",
        link = "https://www.android.com",
        tags = listOf("tag1", "tag2"),
        usedBy = listOf(
            Dependency(system0, "0 used by 1")
        ),
    )
    private val system2 = model.softwareSystem(
        "Software System 2",
        "Description 2",
        Location.Internal,
        icon = "docker",
        link = "https://www.docker.com/",
        properties = C4Properties(
            headers = listOf("Property", "Value"),
            values = listOf(
                listOf("prop key0", "prop value0"),
                listOf("prop key1", "prop value1"),
                listOf("prop key2", "prop value2")
            )
        ),
        uses = listOf(
            Dependency(system0, "2 uses 1")
        )
    )
    private val person = model.person(
        "Actor",
        link = "https://www.google.de",
        tags = listOf("human"),
        uses = listOf(
            Dependency(system1, "creates", "HTTP"),
            Dependency(system0, "deletes", "gRPC")
        ),
        properties = C4Properties(values = listOf(listOf("prop 1", "value 1")))
    )

    @BeforeEach
    fun beforeEach() {
        model.enterprise = Enterprise("My Enterprise")
    }

    @Test
    fun `landscape diagram is written to plant uml as expected`() {
        val diagramName = "SystemLandscape"
        val expectedDiagramContent =
            this::class.java.getResource("/expected/$diagramName.puml")!!.readText(Charsets.UTF_8)
        val landscapeView = workspace.views.createSystemLandscapeView(
            diagramName,
            "A test Landscape",
            C4PlantUmlLayout(layout = Layout.LEFT_TO_RIGHT, legend = Legend.SHOW_FLOATING_LEGEND)
        )
        landscapeView.addAllElements()

        val diagramFolder = File(tempDir, "./diagram/")
        C4PlantUmlDiagramWriter.writeDiagrams(
            diagramFolder,
            workspace
        )

        assertThat(diagramFolder.isDirectory).isTrue
        val actualDiagramFile = File(diagramFolder, "${landscapeView.key}.puml")
        assertThat(actualDiagramFile.isFile).isTrue
        assertThat(actualDiagramFile.readText(Charsets.UTF_8)).isEqualToIgnoringWhitespace(expectedDiagramContent)
    }

    @Test
    fun `context diagram is written to plant uml as expected`() {
        val diagramName = "Context"
        val expectedDiagramContent =
            this::class.java.getResource("/expected/$diagramName.puml")!!.readText(Charsets.UTF_8)
        val contextView = workspace.views.createSystemContextView(
            system1,
            diagramName,
            "A test Landscape",
            C4PlantUmlLayout(
                layout = Layout.LEFT_TO_RIGHT,
                legend = Legend.SHOW_FLOATING_LEGEND,
                hideStereotypes = false
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
