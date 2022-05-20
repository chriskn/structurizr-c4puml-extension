package com.github.chriskn.structurizrextension

import com.github.chriskn.structurizrextension.model.C4Properties
import com.github.chriskn.structurizrextension.model.C4Type
import com.github.chriskn.structurizrextension.model.Dependency
import com.github.chriskn.structurizrextension.model.container
import com.github.chriskn.structurizrextension.model.person
import com.github.chriskn.structurizrextension.model.softwareSystem
import com.github.chriskn.structurizrextension.plantuml.C4PlantUmlDiagramWriter
import com.github.chriskn.structurizrextension.plantuml.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.plantuml.layout.Layout
import com.github.chriskn.structurizrextension.plantuml.layout.Legend
import com.structurizr.Workspace
import com.structurizr.model.CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategy
import com.structurizr.model.Location
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class ContainerViewTest {

    @TempDir
    private lateinit var tempDir: File

    private val diagramName = "Container"

    private val expectedDiagramContent =
        this::class.java.getResource("/expected/$diagramName.puml")!!.readText(Charsets.UTF_8)

    @Test
    fun `container diagram is written to plant uml as expected`() {
        val workspace = Workspace("My Workspace", "")
        val model = workspace.model
        model.impliedRelationshipsStrategy = CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategy()
        val properties = C4Properties(values = listOf(listOf("prop 1", "value 1")))
        val softwareSystem = model.softwareSystem(
            "My Software System",
            "system description",
            link = "https://www.google.de"
        )
        val backendApplication = softwareSystem.container(
            name = "Backend App",
            description = "some backend app",
            technology = "Kotlin",
            tags = listOf("Tag2"),
            icon = "docker",
            link = "https://www.google.de",
            properties = properties
        )
        val app = softwareSystem.container(
            name = "App",
            description = "android app",
            technology = "Android",
            icon = "android",
            uses = listOf(Dependency(backendApplication, "desc", "Soap"))
        )
        softwareSystem.container(
            name = "Database",
            description = "some database",
            type = C4Type.DATABASE,
            technology = "postgres",
            icon = "postgresql",
            usedBy = listOf(Dependency(backendApplication, "CRUD", "JPA"))
        )
        model.person(
            name = "Maintainer",
            description = "some employee",
            location = Location.Internal,
            link = "https://www.google.de",
            uses = listOf(
                Dependency(backendApplication, "Admin UI", "REST")
            ),
            properties = properties
        )
        val broker = model.softwareSystem(
            "Broker",
            "Message Broker",
            Location.External,
            C4Type.QUEUE,
            icon = "kafka",
        )
        broker.container(
            "Topic: my.topic",
            "external topic",
            type = C4Type.QUEUE,
            icon = "kafka",
            usedBy = listOf(
                Dependency(backendApplication, "reads topic", "Avro")
            )
        )
        model.person(
            "User",
            "some user",
            location = Location.External,
            icon = "android",
            uses = listOf(Dependency(app, "uses app"))
        )
        val containerView = workspace.views.containerView(
            softwareSystem,
            diagramName,
            "Test container view",
            C4PlantUmlLayout(legend = Legend.SHOW_STATIC_LEGEND, layout = Layout.TOP_DOWN)
        )
        containerView.addAllElements()
        containerView.externalSoftwareSystemBoundariesVisible = true
        containerView.addAllContainersAndInfluencers()

        val diagramFolder = File(tempDir, "./diagram/")
        C4PlantUmlDiagramWriter.writeDiagrams(
            diagramFolder,
            workspace
        )

        assertThat(diagramFolder.isDirectory).isTrue
        val actualDiagramFile = File(diagramFolder, "${containerView.key}.puml")
        assertThat(actualDiagramFile.isFile).isTrue
        assertThat(actualDiagramFile.readText(Charsets.UTF_8)).isEqualToIgnoringWhitespace(expectedDiagramContent)
    }
}
