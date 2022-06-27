package com.github.chriskn.structurizrextension

import com.github.chriskn.structurizrextension.model.C4Type
import com.github.chriskn.structurizrextension.model.Dependency
import com.github.chriskn.structurizrextension.model.component
import com.github.chriskn.structurizrextension.model.container
import com.github.chriskn.structurizrextension.model.softwareSystem
import com.github.chriskn.structurizrextension.plantuml.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.plantuml.layout.Legend
import com.structurizr.Workspace
import com.structurizr.model.Container
import com.structurizr.view.DynamicView
import org.junit.jupiter.api.Test

class DynamicViewTest {

    private val workspace = Workspace("My Workspace", "Some Description")
    private val model = workspace.model
    private val system1 = model.softwareSystem(
        name = "Software System 1",
        description = "Description 1",
    )
    private val singlePageApplication: Container = system1.container(
        "Single-Page Application",
        "Provides all of the functionality to customers via their web browser.",
        technology = "JavaScript and Angular",
        icon = "Docker"
    )
    private val apiApplication: Container = system1.container(
        "API Application",
        "Provides functionality via a JSON/HTTPS API.",
        technology = "Java and Spring MVC",
        usedBy = listOf(Dependency(singlePageApplication, "gets data from"))
    )
    private val database: Container = system1.container(
        "Database",
        "Stores user registration information",
        technology = "Oracle Database Schema",
        c4Type = C4Type.DATABASE,
        usedBy = listOf(Dependency(apiApplication, "stores data to"))
    )
    private val loginController = apiApplication.component(
        "Sign In Controller",
        "Allows users to sign in.",
        technology = "Spring MVC Rest Controller",
        usedBy = listOf(
            Dependency(singlePageApplication, "")
        )
    )
    private val securityComponent = apiApplication.component(
        "Security Component",
        "Provides functionality related to signing in, changing passwords, etc.",
        technology = "Spring Bean",
        usedBy = listOf(
            Dependency(loginController, ""),
        ),
        uses = listOf(
            Dependency(database, ""),
        )
    )

    private fun addElements(dynamicView: DynamicView) {
        dynamicView.add(singlePageApplication, "Submits credentials to", loginController)
        dynamicView.add(loginController, "Validates credentials using", securityComponent)
        dynamicView.add(securityComponent, "select * from users where username = ?", database)
        dynamicView.add(database, "Returns user data to", securityComponent)
        dynamicView.add(securityComponent, "Returns true if the hashed password matches", loginController)
        dynamicView.add(loginController, "Sends back an authentication token to", singlePageApplication)
    }

    @Test
    fun `test interaction diagram for container is written to plant uml as expected`() {
        val diagramKey = "DynamicContainer"
        val expectedDiagramContent =
            this::class.java.getResource("/expected/$diagramKey.puml")!!.readText(Charsets.UTF_8)
        val dynamicView: DynamicView = workspace.views.dynamicView(
            apiApplication,
            diagramKey,
            "description",
            C4PlantUmlLayout(legend = Legend.ShowFloatingLegend)
        )
        addElements(dynamicView)

        assertExpectedDiagramWasWrittenForView(workspace, diagramKey, expectedDiagramContent)
    }

    @Test
    fun `test interaction diagram for system is written to plant uml as expected`() {
        val diagramKey = "DynamicSystem"
        val expectedDiagramContent =
            this::class.java.getResource("/expected/$diagramKey.puml")!!.readText(Charsets.UTF_8)
        val dynamicView: DynamicView = workspace.views.dynamicView(
            system1,
            diagramKey,
            "description",
            C4PlantUmlLayout(legend = Legend.ShowFloatingLegend)
        )
        dynamicView.add(singlePageApplication, "gets data from", apiApplication)
        dynamicView.add(apiApplication, "stores data to", database)

        assertExpectedDiagramWasWrittenForView(workspace, diagramKey, expectedDiagramContent)
    }
}
