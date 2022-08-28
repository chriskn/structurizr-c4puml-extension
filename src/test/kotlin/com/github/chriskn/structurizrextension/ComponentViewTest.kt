package com.github.chriskn.structurizrextension

import com.github.chriskn.structurizrextension.model.C4Properties
import com.github.chriskn.structurizrextension.model.C4Type
import com.github.chriskn.structurizrextension.model.Dependency
import com.github.chriskn.structurizrextension.model.component
import com.github.chriskn.structurizrextension.model.container
import com.github.chriskn.structurizrextension.model.person
import com.github.chriskn.structurizrextension.model.usedBy
import com.github.chriskn.structurizrextension.model.uses
import com.github.chriskn.structurizrextension.plantuml.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.plantuml.Layout
import com.github.chriskn.structurizrextension.plantuml.LineType
import com.github.chriskn.structurizrextension.view.componentView
import com.structurizr.Workspace
import com.structurizr.model.InteractionStyle
import com.structurizr.model.Location
import org.junit.jupiter.api.Test

class ComponentViewTest {

    private val diagramKey = "Component"

    private val expectedDiagramContent =
        this::class.java.getResource("/expected/$diagramKey.puml")!!.readText(Charsets.UTF_8)

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
            technology = "REST"
        )
        restController.usedBy(user, "Website", "REST")
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
        )
        service.uses(
            repository,
            "gets notified",
            interactionStyle = InteractionStyle.Asynchronous,
        )
        val backendApp = backendApplication.component(
            "Cache",
            "In Memory DB",
            link = "https://google.de",
            technology = "RocksDB",
            icon = "rocksdb",
            c4Type = C4Type.DATABASE,
        )
        backendApp.usedBy(service, "uses", link = "")
        softwareSystem.container(
            "Database",
            "some database",
            c4Type = C4Type.DATABASE,
            technology = "postgres",
            icon = "postgresql",
            usedBy = listOf(Dependency(backendApplication.components.first { it.hasTag("repo") }, "gets data from"))
        )
        val maintainer = model.person(
            "Maintainer",
            "some employee",
            location = Location.Internal
        )
        maintainer.uses(restController, "Admin UI", "REST", icon = "empty")
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
        componentView.addAllElements()

        assertExpectedDiagramWasWrittenForView(workspace, diagramKey, expectedDiagramContent)
    }
}
