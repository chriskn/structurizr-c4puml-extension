package com.github.chriskn.structurizrextension

import com.github.chriskn.structurizrextension.model.C4Properties
import com.github.chriskn.structurizrextension.model.C4Type
import com.github.chriskn.structurizrextension.model.Dependency
import com.github.chriskn.structurizrextension.model.container
import com.github.chriskn.structurizrextension.model.person
import com.github.chriskn.structurizrextension.model.softwareSystem
import com.github.chriskn.structurizrextension.plantuml.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.plantuml.DependencyConfiguration
import com.github.chriskn.structurizrextension.plantuml.Direction
import com.github.chriskn.structurizrextension.plantuml.Layout
import com.github.chriskn.structurizrextension.plantuml.Legend
import com.github.chriskn.structurizrextension.plantuml.LineType
import com.github.chriskn.structurizrextension.view.containerView
import com.structurizr.Workspace
import com.structurizr.model.InteractionStyle
import com.structurizr.model.Location
import org.junit.jupiter.api.Test

class ContainerViewTest {

    val workspace = Workspace("My Workspace", "")
    val model = workspace.model
    val properties = C4Properties(values = listOf(listOf("prop 1", "value 1")))
    val softwareSystem = model.softwareSystem(
        name = "My Software System",
        description = "system description",
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
        c4Type = C4Type.DATABASE,
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
        name = "Broker",
        description = "Message Broker",
        location = Location.External,
        c4Type = C4Type.QUEUE,
        icon = "kafka",
    )
    val topic = broker.container(
        "my.topic",
        "external topic",
        c4Type = C4Type.QUEUE,
        icon = "kafka",
        usedBy = listOf(
            Dependency(backendApplication, "reads topic", "Avro", interactionStyle = InteractionStyle.Asynchronous)
        )
    )
    val graphql = model.softwareSystem(
        name = "GraphQL",
        description = "Federated GraphQL",
        location = Location.External,
        icon = "graphql"
    )
    val internalSchema = graphql.container(
        name = "Internal Schema",
        description = "Schema provided by our app",
        location = Location.Internal,
        usedBy = listOf(
            Dependency(backendApplication, "provides subgraph to"),
            Dependency(app, "reuqest data using", "GraphQL", icon = "graphql", link = "https://graphql.org/")
        )
    )
    val externalSchema = graphql.container(
        name = "External Schema",
        description = "Schema provided by another team",
        uses = listOf(Dependency(internalSchema, "extends schema"))
    )
    val androidUser = model.person(
        name = "Android User",
        description = "some Android user",
        location = Location.External,
        icon = "android",
        uses = listOf(Dependency(app, "uses app"))
    )

    @Test
    fun `container diagram is written as expected with external boundary`() {
        val diagramKey = "ContainerWithBoundary"
        val containerView = workspace.views.containerView(
            softwareSystem,
            diagramKey,
            "Example container view",
            C4PlantUmlLayout(
                legend = Legend.ShowLegend,
                layout = Layout.TopDown,
                lineType = LineType.Ortho,
                nodeSep = 100,
                rankSep = 130,
                dependencyConfigurations = listOf(
                    DependencyConfiguration(filter = { it.destination == database }, direction = Direction.Right),
                    DependencyConfiguration(filter = { it.destination == topic }, direction = Direction.Up)
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

        assertExpectedDiagramWasWrittenForView(workspace, diagramKey)
    }

    @Test
    fun `container diagram is written as expected without external boundary`() {
        val diagramKey = "ContainerWithoutBoundary"
        val containerView = workspace.views.containerView(
            softwareSystem,
            diagramKey,
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

        assertExpectedDiagramWasWrittenForView(workspace, diagramKey)
    }
}
