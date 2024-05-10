package com.github.chriskn.structurizrextension.view

import com.github.chriskn.structurizrextension.assertExpectedDiagramWasWrittenForView
import com.github.chriskn.structurizrextension.model.C4Properties
import com.github.chriskn.structurizrextension.model.C4Type
import com.github.chriskn.structurizrextension.model.Dependency
import com.github.chriskn.structurizrextension.model.component
import com.github.chriskn.structurizrextension.model.container
import com.github.chriskn.structurizrextension.model.person
import com.github.chriskn.structurizrextension.model.softwareSystem
import com.github.chriskn.structurizrextension.plantuml.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.plantuml.Layout
import com.github.chriskn.structurizrextension.plantuml.LineType
import com.structurizr.Workspace
import com.structurizr.model.InteractionStyle
import com.structurizr.model.Location
import org.junit.jupiter.api.Test

class ComponentViewTest {

    private val pathToExpectedDiagrams = "view/component"

    private val workspace = Workspace("My Workspace", "")
    private val model = workspace.model
    private val softwareSystem = model.softwareSystem("My Software System", "system description")
    private val backendApplication = softwareSystem.container("Backend App", "some backend app", technology = "Kotlin")

    init {
        val user = model.person("User", "A user", Location.External)
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
        val service = backendApplication.component(
            "MyService",
            "Does implement some logic",
            link = "https://google.de",
            technology = "",
            icon = "kotlin",
            usedBy = listOf(Dependency(restController, "calls")),
            uses = listOf(
                Dependency(
                    repository,
                    "gets notified",
                    interactionStyle = InteractionStyle.Asynchronous,
                )
            )
        )
        backendApplication.component(
            "Cache",
            "In Memory DB",
            link = "https://google.de",
            technology = "RocksDB",
            icon = "rocksdb",
            c4Type = C4Type.DATABASE,
            usedBy = listOf(Dependency(service, "uses", link = ""))
        )
        softwareSystem.container(
            "Database",
            "some database",
            c4Type = C4Type.DATABASE,
            technology = "postgres",
            icon = "postgresql",
            usedBy = listOf(Dependency(backendApplication.components.first { it.hasTag("repo") }, "gets data from"))
        )
        model.person(
            "Maintainer",
            "some employee",
            location = Location.Internal,
            uses = listOf(Dependency(restController, "Admin UI", "REST", icon = "empty"))
        )
    }

    @Test
    fun `component diagram is written without boundary if it contains only components`() {
        val diagramKey = "ComponentWithoutBoundary"
        val componentView = workspace.views.componentView(
            backendApplication,
            diagramKey,
            "Test component view",
            C4PlantUmlLayout(
                nodeSep = 100,
                rankSep = 150,
                lineType = LineType.Ortho,
                layout = Layout.LeftToRight
            )
        )
        componentView.addAllComponents()

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `component diagram is written with boundary if container boundaries are visible`() {
        val diagramKey = "ComponentWithBoundary"
        val componentView = workspace.views.componentView(
            backendApplication,
            diagramKey,
            "Test component view",
            C4PlantUmlLayout(
                nodeSep = 100,
                rankSep = 150,
                lineType = LineType.Ortho,
                layout = Layout.LeftToRight
            )
        )
        componentView.addAllComponents()
        componentView.showExternalContainerBoundaries = true

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `component diagram is written without boundary if containers are added and boundaries not visible`() {
        val diagramKey = "ComponentWithContainers"
        val componentView = workspace.views.componentView(
            backendApplication,
            diagramKey,
            "Test component view",
            C4PlantUmlLayout(
                nodeSep = 100,
                rankSep = 150,
                lineType = LineType.Ortho,
                layout = Layout.LeftToRight
            )
        )
        componentView.addAllComponents()
        componentView.addAllContainers()

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `component diagram is written with boundary if containers are added and  container boundaries are visible`() {
        val diagramKey = "ComponentWithContainersAndBoundaries"
        val componentView = workspace.views.componentView(
            backendApplication,
            diagramKey,
            "Test component view",
            C4PlantUmlLayout(
                nodeSep = 100,
                rankSep = 150,
                lineType = LineType.Ortho,
                layout = Layout.LeftToRight
            )
        )
        componentView.addAllComponents()
        componentView.addAllContainers()
        componentView.showExternalContainerBoundaries = true

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }
}
