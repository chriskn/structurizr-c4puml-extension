package com.github.chriskn.structurizrextension.view

import com.github.chriskn.structurizrextension.api.model.C4Properties
import com.github.chriskn.structurizrextension.api.model.C4Type
import com.github.chriskn.structurizrextension.api.model.Dependency
import com.github.chriskn.structurizrextension.api.model.component
import com.github.chriskn.structurizrextension.api.model.container
import com.github.chriskn.structurizrextension.api.model.person
import com.github.chriskn.structurizrextension.api.model.softwareSystem
import com.github.chriskn.structurizrextension.api.view.componentView
import com.github.chriskn.structurizrextension.api.view.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.api.view.layout.Layout.LeftToRight
import com.github.chriskn.structurizrextension.api.view.layout.LineType.Ortho
import com.github.chriskn.structurizrextension.api.view.showExternalContainerBoundaries
import com.github.chriskn.structurizrextension.api.view.sprite.library.SpriteLibrary
import com.github.chriskn.structurizrextension.assertExpectedDiagramWasWrittenForView
import com.structurizr.Workspace
import com.structurizr.model.InteractionStyle.Asynchronous
import com.structurizr.model.Location
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class ComponentViewTest {

    private val pathToExpectedDiagrams = "view/component"

    private val workspace = Workspace("My Workspace", "")
    private val model = workspace.model

    private val softwareSystem = model.softwareSystem("My Software System", "system description")
    private val backendApplication = softwareSystem.container(
        "New Backend App",
        "some backend app",
        technology = "Kotlin"
    )
    private val frontendApplication = softwareSystem.container("Frontend App", "some frontend", technology = "TS")

    @BeforeAll
    fun setUpModel() {
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
            sprite = SpriteLibrary.spriteByName("logos-kotlin-icon"),
            usedBy = listOf(Dependency(restController, "calls")),
            uses = listOf(
                Dependency(
                    repository,
                    "gets notified",
                    interactionStyle = Asynchronous,
                )
            )
        )
        backendApplication.component(
            "Cache",
            "In Memory DB",
            link = "https://google.de",
            technology = "RocksDB",
            sprite = SpriteLibrary.spriteByName("tupadr3-devicons2-rocksdb"),
            c4Type = C4Type.DATABASE,
            usedBy = listOf(Dependency(service, "uses", link = ""))
        )
        frontendApplication.component(
            name = "SPA",
            description = "Single Page Application",
            uses = listOf(Dependency(restController, "gets data from"))
        )
        softwareSystem.container(
            "Database",
            "some database",
            c4Type = C4Type.DATABASE,
            technology = "postgres",
            sprite = SpriteLibrary.spriteByName("logos-postgresql"),
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
                lineType = Ortho,
                layout = LeftToRight
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
                lineType = Ortho,
                layout = LeftToRight
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
                lineType = Ortho,
                layout = LeftToRight
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
                lineType = Ortho,
                layout = LeftToRight
            )
        )
        componentView.addAllComponents()
        frontendApplication.components.forEach { componentView.add(it) }
        componentView.addAllContainers()
        componentView.showExternalContainerBoundaries = true

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }
}
