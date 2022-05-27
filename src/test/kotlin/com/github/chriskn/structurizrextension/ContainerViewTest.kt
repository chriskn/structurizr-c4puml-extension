package com.github.chriskn.structurizrextension

import com.github.chriskn.structurizrextension.model.C4Properties
import com.github.chriskn.structurizrextension.model.C4Type
import com.github.chriskn.structurizrextension.model.Dependency
import com.github.chriskn.structurizrextension.model.container
import com.github.chriskn.structurizrextension.model.person
import com.github.chriskn.structurizrextension.model.softwareSystem
import com.github.chriskn.structurizrextension.plantuml.C4PlantUmlDiagramWriter
import com.github.chriskn.structurizrextension.plantuml.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.plantuml.layout.DependencyConfiguration
import com.github.chriskn.structurizrextension.plantuml.layout.Direction
import com.github.chriskn.structurizrextension.plantuml.layout.Layout
import com.github.chriskn.structurizrextension.plantuml.layout.Legend
import com.structurizr.Workspace
import com.structurizr.io.plantuml.C4PlantUMLWriter.Directions
import com.structurizr.model.Location
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class ContainerViewTest {

    @TempDir
    private lateinit var tempDir: File

    val workspace = Workspace("My Workspace", "")
    val model = workspace.model
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
    )
    val database = softwareSystem.container(
        name = "Database",
        description = "some database",
        type = C4Type.DATABASE,
        technology = "postgres",
        icon = "postgresql",
        usedBy = listOf(Dependency(backendApplication, "CRUD", "JPA"))
    )
    val maintainer = model.person(
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
    val topic = broker.container(
        "Topic: my.topic",
        "external topic",
        type = C4Type.QUEUE,
        icon = "kafka",
        usedBy = listOf(
            Dependency(backendApplication, "reads topic", "Avro")
        )
    )
    val graphql = model.softwareSystem(
        "GraphQL",
        "Federated GraphQL",
        Location.External,
        icon = "graphql"
    )
    val internalSchema = graphql.container(
        "Internal Schema",
        "Schema provided by our app",
        Location.Internal,
        usedBy = listOf(
            Dependency(backendApplication, "provides subgraph to"),
            Dependency(app, "reuqest data using", "GraphQL")
        )
    )
    val externalSchema = graphql.container(
        "External Schema",
        "Schema provided by another team",
        uses = listOf(Dependency(internalSchema, "extends schema"))
    )
    val user = model.person(
        "User",
        "some user",
        location = Location.External,
        icon = "android",
        uses = listOf(Dependency(app, "uses app"))
    )

    @Test
    fun `container diagram is written as expected with external boundary`() {
        val diagramName = "ContainerWithBoundary"
        val expectedDiagramContent =
            this::class.java.getResource("/expected/$diagramName.puml")!!.readText(Charsets.UTF_8)
        val containerView = workspace.views.containerView(
            softwareSystem,
            diagramName,
            "Test container view",
            C4PlantUmlLayout(
                legend = Legend.ShowStaticLegend,
                layout = Layout.TopDown,
                dependencyConfigurations = listOf(
                    DependencyConfiguration(filter = { it.destination == database }, direction = Direction.Right)
                )
            )
        )
        containerView.addAllContainers()
        containerView.externalSoftwareSystemBoundariesVisible = true
        containerView.add(topic)
        containerView.add(internalSchema)
        containerView.add(externalSchema)

        containerView.addDependentSoftwareSystems()
        containerView.addAllPeople()

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

    @Test
    fun `container diagram is written as expected without external boundary`() {
        val diagramName = "ContainerWithoutBoundary"
        val expectedDiagramContent =
            this::class.java.getResource("/expected/$diagramName.puml")!!.readText(Charsets.UTF_8)
        val containerView = workspace.views.containerView(
            softwareSystem,
            diagramName,
            "Test container view",
            C4PlantUmlLayout(
                legend = Legend.None,
                layout = Layout.TopDown,
                showPersonOutline = false,
                dependencyConfigurations = listOf(
                    DependencyConfiguration(filter = { it.destination == database }, direction = Direction.Right),
                    DependencyConfiguration(filter = { it.source == externalSchema }, direction = Direction.Up)
                )
            )
        )
        containerView.addAllContainers()
        containerView.add(topic)
        containerView.add(internalSchema)
        containerView.add(externalSchema)

        containerView.addDependentSoftwareSystems()
        containerView.addAllPeople()

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
