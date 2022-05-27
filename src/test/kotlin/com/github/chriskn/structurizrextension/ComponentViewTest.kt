package com.github.chriskn.structurizrextension

import com.github.chriskn.structurizrextension.model.C4Properties
import com.github.chriskn.structurizrextension.model.C4Type
import com.github.chriskn.structurizrextension.model.Dependency
import com.github.chriskn.structurizrextension.model.component
import com.github.chriskn.structurizrextension.model.container
import com.github.chriskn.structurizrextension.model.person
import com.github.chriskn.structurizrextension.plantuml.C4PlantUmlDiagramWriter
import com.github.chriskn.structurizrextension.plantuml.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.plantuml.layout.Layout
import com.github.chriskn.structurizrextension.plantuml.layout.LineType
import com.structurizr.Workspace
import com.structurizr.model.InteractionStyle
import com.structurizr.model.Location
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class ComponentViewTest {

    @TempDir
    private lateinit var tempDir: File

    private val diagramName = "Component"

    private val expectedDiagramContent =
        this::class.java.getResource("/expected/$diagramName.puml")!!.readText(Charsets.UTF_8)

    @Test
    fun `component diagram is written as expected`() {
        val workspace = Workspace("My Workspace", "")
        val model = workspace.model
        val softwareSystem = model.addSoftwareSystem("My Software System", "system description")
        val backendApplication = softwareSystem.addContainer("Backend App", "some backend app", "Kotlin")
        val user = model.addPerson(Location.External, "User", "A user")
        val restController = backendApplication.component(
            "MyRestController",
            "Provides data via rest",
            technology = "REST",
            usedBy = listOf(Dependency(user, "Website", "REST"))
        )
        val repository = backendApplication.component(
            "MyRepo",
            "Provides CRUD operations for data",
            technology = "Kotlin, JDBC",
            tags = listOf("repo", "persistence"),
            properties = C4Properties(values = listOf(listOf("jdbcUrl", "someurl")))
        )
        backendApplication.component(
            "MyService",
            "Does implement some logic",
            link = "https://google.de",
            technology = "",
            icon = "kotlin",
            usedBy = listOf(Dependency(restController, "calls")),
            uses = listOf(Dependency(repository, "gets notified", interactionStyle = InteractionStyle.Asynchronous))
        )
        softwareSystem.container(
            "Database",
            "some database",
            type = C4Type.DATABASE,
            technology = "postgres",
            icon = "postgresql",
            usedBy = listOf(Dependency(backendApplication.components.first { it.hasTag("repo") }, "gets data from"))
        )
        model.person(
            "Maintainer",
            "some employee",
            location = Location.Internal
        ).uses(restController, "Admin UI", "REST")

        val componentView = workspace.views.componentView(
            backendApplication,
            diagramName,
            "Test component view",
            C4PlantUmlLayout(
                nodeSep = 100,
                rankSep = 150,
                lineType = LineType.Ortho,
                layout = Layout.LeftToRight
            )
        )
        componentView.addAllElements()

        val diagramFolder = File(tempDir, "./diagram/")
        C4PlantUmlDiagramWriter.writeDiagrams(
            diagramFolder,
            workspace
        )

        assertThat(diagramFolder.isDirectory).isTrue
        val actualDiagramFile = File(diagramFolder, "${componentView.key}.puml")
        assertThat(actualDiagramFile.isFile).isTrue
        assertThat(actualDiagramFile.readText(Charsets.UTF_8)).isEqualToIgnoringWhitespace(expectedDiagramContent)
    }
}
