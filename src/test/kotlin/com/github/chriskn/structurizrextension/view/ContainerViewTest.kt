package com.github.chriskn.structurizrextension.view

import com.github.chriskn.structurizrextension.api.model.C4Properties
import com.github.chriskn.structurizrextension.api.model.C4Type
import com.github.chriskn.structurizrextension.api.model.Dependency
import com.github.chriskn.structurizrextension.api.model.container
import com.github.chriskn.structurizrextension.api.model.person
import com.github.chriskn.structurizrextension.api.model.softwareSystem
import com.github.chriskn.structurizrextension.api.view.containerView
import com.github.chriskn.structurizrextension.api.view.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.api.view.layout.DependencyConfiguration
import com.github.chriskn.structurizrextension.api.view.layout.Direction.Right
import com.github.chriskn.structurizrextension.api.view.layout.Direction.Up
import com.github.chriskn.structurizrextension.api.view.layout.Layout.TopDown
import com.github.chriskn.structurizrextension.api.view.layout.Legend.None
import com.github.chriskn.structurizrextension.api.view.layout.Legend.ShowLegend
import com.github.chriskn.structurizrextension.api.view.layout.LineType.Ortho
import com.github.chriskn.structurizrextension.api.view.showExternalSoftwareSystemBoundaries
import com.github.chriskn.structurizrextension.api.view.sprite.PlantUmlSprite
import com.github.chriskn.structurizrextension.api.view.sprite.registry.SpriteRegistry
import com.github.chriskn.structurizrextension.assertExpectedDiagramWasWrittenForView
import com.structurizr.Workspace
import com.structurizr.model.InteractionStyle.Asynchronous
import com.structurizr.model.Location
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class ContainerViewTest {

    private val pathToExpectedDiagrams = "view/container"

    private val workspace = Workspace("My Workspace", "")
    private val model = workspace.model

    private val properties = C4Properties(values = listOf(listOf("prop 1", "value 1")))
    private val softwareSystem = model.softwareSystem(
        name = "My Software System",
        description = "system description",
        link = "https://www.google.de"
    )
    private val backendApplication = softwareSystem.container(
        name = "Backend App",
        description = "some backend app",
        technology = "Kotlin",
        sprite = SpriteRegistry.spriteByName("logos-docker-icon"),
        link = "https://www.google.de",
        properties = properties
    )
    private val app = softwareSystem.container(
        name = "App",
        description = "Azure app",
        technology = "Some Service",
        sprite = (SpriteRegistry.spriteByName("Azure-AIMachineLearning-AzureTranslatorText") as PlantUmlSprite).copy(
            color = "white"
        ),
    )
    private val graphql = model.softwareSystem(
        name = "GraphQL",
        description = "Federated GraphQL",
        location = Location.External,
        sprite = SpriteRegistry.spriteByName("logos-graphql"),
    )
    private val internalSchema = graphql.container(
        name = "Internal Schema",
        description = "Schema provided by our app",
        location = Location.Internal,
        usedBy = listOf(
            Dependency(backendApplication, "provides subgraph to"),
            Dependency(
                app,
                "request data using",
                "GraphQL",
                sprite = SpriteRegistry.spriteByName("tupadr3-devicons2-graphql"),
                link = "https://graphql.org/"
            )
        )
    )
    private val externalSchema = graphql.container(
        name = "External Schema",
        description = "Schema provided by another team",
        uses = listOf(Dependency(internalSchema, "extends schema"))
    )
    private val broker = model.softwareSystem(
        name = "Broker",
        description = "Message Broker",
        location = Location.External,
        c4Type = C4Type.QUEUE,
        sprite = SpriteRegistry.spriteByName("logos-kafka"),
    )
    private val topic = broker.container(
        "my.topic",
        "external topic",
        c4Type = C4Type.QUEUE,
        sprite = SpriteRegistry.spriteByName("logos-kafka-img"),
        usedBy = listOf(
            Dependency(backendApplication, "reads topic", "Avro", interactionStyle = Asynchronous)
        )
    )
    private val database = softwareSystem.container(
        name = "Database",
        description = "some database",
        c4Type = C4Type.DATABASE,
        technology = "postgres",
        sprite = SpriteRegistry.spriteByName("logos-postgresql-img"),
        usedBy = listOf(Dependency(backendApplication, "CRUD", "JPA"))
    )

    @BeforeAll
    fun finalizeModel() {
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
        model.person(
            name = "Android User",
            description = "some Android user",
            location = Location.External,
            sprite = SpriteRegistry.spriteByName("logos-android-icon"),
            uses = listOf(Dependency(app, "uses app"))
        )
        model.softwareSystem(
            name = "Third Party System",
            description = "External System",
            location = Location.External,
            usedBy = listOf(Dependency(backendApplication, "uses"))
        )
    }

    @Test
    fun `container diagram is written without boundary if it contains only containers`() {
        val diagramKey = "ContainerWithoutBoundary"
        val containerView = workspace.views.containerView(
            softwareSystem,
            diagramKey,
            "Test container view",
            C4PlantUmlLayout(
                legend = None,
                layout = TopDown,
                showPersonOutline = false,
                dependencyConfigurations = listOf(
                    DependencyConfiguration(filter = { it.destination == database }, direction = Right),
                    DependencyConfiguration(filter = { it.source == externalSchema }, direction = Up)
                )
            )
        )

        containerView.addAllContainers()
        containerView.add(topic)
        containerView.add(internalSchema)
        containerView.add(externalSchema)
        containerView.addAllPeople()

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `container diagram is written with boundary if system boundaries are visible`() {
        val diagramKey = "ContainerWithBoundary"
        val containerView = workspace.views.containerView(
            softwareSystem,
            diagramKey,
            "Example container view",
            C4PlantUmlLayout(
                legend = ShowLegend,
                layout = TopDown,
                lineType = Ortho,
                nodeSep = 100,
                rankSep = 130,
                dependencyConfigurations = listOf(
                    DependencyConfiguration(filter = { it.destination == database }, direction = Right),
                    DependencyConfiguration(filter = { it.destination == topic }, direction = Up)
                )
            )
        )

        containerView.addAllContainers()
        containerView.add(topic)
        containerView.add(internalSchema)
        containerView.add(externalSchema)
        containerView.addAllSoftwareSystems()
        containerView.addAllPeople()

        containerView.showExternalSoftwareSystemBoundaries = true

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `container diagram is written without boundary if systems are added and boundaries not visible`() {
        val diagramKey = "ContainerWithSystems"
        val containerView = workspace.views.containerView(
            softwareSystem,
            diagramKey,
            "Test container view",
            C4PlantUmlLayout(
                legend = None,
                layout = TopDown,
                showPersonOutline = false,
                dependencyConfigurations = listOf(
                    DependencyConfiguration(filter = { it.destination == database }, direction = Right),
                    DependencyConfiguration(filter = { it.source == externalSchema }, direction = Up)
                )
            )
        )
        containerView.addAllContainers()
        containerView.add(topic)
        containerView.add(internalSchema)
        containerView.add(externalSchema)
        containerView.addAllSoftwareSystems()
        containerView.addAllPeople()

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `container diagram is written with boundary if systems are added and system boundaries are visible`() {
        val diagramKey = "ContainerWithSystemsAndBoundaries"
        val containerView = workspace.views.containerView(
            softwareSystem,
            diagramKey,
            "Example container view",
            C4PlantUmlLayout(
                legend = ShowLegend,
                layout = TopDown,
                lineType = Ortho,
                nodeSep = 100,
                rankSep = 130,
                dependencyConfigurations = listOf(
                    DependencyConfiguration(filter = { it.destination == database }, direction = Right),
                    DependencyConfiguration(filter = { it.destination == topic }, direction = Up)
                )
            )
        )
        containerView.addAllContainers()
        containerView.add(topic)
        containerView.add(internalSchema)
        containerView.add(externalSchema)
        containerView.addAllSoftwareSystems()
        containerView.addAllPeople()

        containerView.showExternalSoftwareSystemBoundaries = true

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }
}
