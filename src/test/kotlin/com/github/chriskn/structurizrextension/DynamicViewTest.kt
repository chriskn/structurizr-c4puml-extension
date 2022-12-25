package com.github.chriskn.structurizrextension

import com.github.chriskn.structurizrextension.model.C4Type
import com.github.chriskn.structurizrextension.model.Dependency
import com.github.chriskn.structurizrextension.model.component
import com.github.chriskn.structurizrextension.model.container
import com.github.chriskn.structurizrextension.model.softwareSystem
import com.github.chriskn.structurizrextension.plantuml.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.plantuml.DependencyConfiguration
import com.github.chriskn.structurizrextension.plantuml.Direction
import com.github.chriskn.structurizrextension.plantuml.Legend
import com.github.chriskn.structurizrextension.view.dynamicView
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
    private val system2 = model.softwareSystem(
        name = "Reporting System",
        description = "Reporting System",
    )
    private val system3 = model.softwareSystem(
        name = "Software System 3",
        description = "Description 3",
    )
    private val reportingDatabase = system2.container(
        name = "Reporting Database",
        description = "Reporting Database",
        technology = "Postgres",
        c4Type = C4Type.DATABASE,
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
        usedBy = listOf(
            Dependency(singlePageApplication, "")
        )
    )
    private val database: Container = system1.container(
        "Database",
        "Stores user registration information",
        technology = "Oracle Database Schema",
        c4Type = C4Type.DATABASE,
        usedBy = listOf(Dependency(apiApplication, ""))
    )
    private val singInController = apiApplication.component(
        "Sign In Controller",
        "Allows users to sign in.",
        technology = "Spring MVC Rest Controller",
        usedBy = listOf(
            Dependency(singlePageApplication, "")
        )
    )
    private val loginDialog = singlePageApplication.component(
        "Login Dialog",
        "Prompts for user credentials.",
        technology = "REACT",
        usedBy = listOf(
            Dependency(singInController, ""),
            Dependency(reportingDatabase, "")
        )
    )
    private val securityComponent = apiApplication.component(
        "Security Component",
        "Provides functionality related to signing in, changing passwords, etc.",
        technology = "Spring Bean",
        usedBy = listOf(Dependency(singInController, "")),
        uses = listOf(
            Dependency(database, ""),
            Dependency(system3, "")
        )
    )

    @Test
    fun `test interaction diagram with all elements is written to plant uml as expected`() {
        val diagramKey = "DynamicWithAllElements"
        val dynamicView: DynamicView = workspace.views.dynamicView(
            apiApplication,
            diagramKey,
            "description",
            C4PlantUmlLayout(
                legend = Legend.ShowFloatingLegend,
                dependencyConfigurations = listOf(
                    DependencyConfiguration(
                        filter = { it.destination == singInController },
                        direction = Direction.Right
                    ),
                    DependencyConfiguration(
                        filter = { it.source == singInController && it.destination != securityComponent },
                        direction = Direction.Left
                    ),
                    DependencyConfiguration(
                        filter = { it.destination == database },
                        direction = Direction.Left
                    ),
                    DependencyConfiguration(
                        filter = { it.source == database },
                        direction = Direction.Right
                    )
                )
            )
        )
        dynamicView.externalBoundariesVisible = true

        dynamicView.add(loginDialog, "Submits credentials to", singInController)
        dynamicView.add(singInController, "Validates credentials using", securityComponent)
        dynamicView.add(securityComponent, "select * from users where username = ?", database)
        dynamicView.add(database, "Returns user data to", securityComponent)
        dynamicView.add(securityComponent, "Returns true if the hashed password matches", singInController)
        dynamicView.add(securityComponent, "Does something", system3)

        dynamicView.add(singInController, "Triggers redirect", loginDialog)
        dynamicView.add(loginDialog, "Reports to", reportingDatabase)

        assertExpectedDiagramWasWrittenForView(workspace, diagramKey)
    }

    @Test
    fun `test interaction diagram for containers is written to plant uml as expected`() {
        val diagramKey = "DynamicWithContainers"
        val dynamicView: DynamicView = workspace.views.dynamicView(
            system1,
            diagramKey,
            "description",
            C4PlantUmlLayout(
                legend = Legend.None,
                dependencyConfigurations = listOf(
                    DependencyConfiguration(
                        filter = { it.destination == database },
                        direction = Direction.Right
                    ),
                    DependencyConfiguration(
                        filter = { it.source == database },
                        direction = Direction.Left
                    )
                )
            )
        )
        dynamicView.externalBoundariesVisible = true

        dynamicView.add(singlePageApplication, "gets data from", apiApplication)
        dynamicView.add(apiApplication, "stores data to", database)
        dynamicView.add(database, "returns data", apiApplication)

        assertExpectedDiagramWasWrittenForView(workspace, diagramKey)
    }

    @Test
    fun `test interaction diagram for components is written to plant uml as expected`() {
        val diagramKey = "DynamicWithComponents"
        val dynamicView: DynamicView = workspace.views.dynamicView(
            apiApplication,
            diagramKey,
            "description"
        )
        // dynamicView.externalBoundariesVisible = true

        dynamicView.add(loginDialog, "Submits credentials to", singInController)
        dynamicView.add(singInController, "Validates credentials using", securityComponent)
        dynamicView.add(securityComponent, "Returns true if the hashed password matches", singInController)
        dynamicView.add(singInController, "Triggers redirect", loginDialog)

        assertExpectedDiagramWasWrittenForView(workspace, diagramKey)
    }
}
