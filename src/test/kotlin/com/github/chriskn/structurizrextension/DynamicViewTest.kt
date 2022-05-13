package com.github.chriskn.structurizrextension

import com.github.chriskn.structurizrextension.model.C4Type
import com.github.chriskn.structurizrextension.model.Dependency
import com.github.chriskn.structurizrextension.model.component
import com.github.chriskn.structurizrextension.model.container
import com.github.chriskn.structurizrextension.model.softwareSystem
import com.github.chriskn.structurizrextension.plantuml.C4PlantUmlDiagramWriter
import com.github.chriskn.structurizrextension.plantuml.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.plantuml.layout.Legend
import com.structurizr.Workspace
import com.structurizr.model.Container
import com.structurizr.view.DynamicView
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class DynamicViewTest {

    @TempDir
    private lateinit var tempDir: File

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
        type = C4Type.DATABASE,
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
        val diagramName = "DynamicContainer"
        val expectedDiagramContent =
            this::class.java.getResource("/expected/$diagramName.puml")!!.readText(Charsets.UTF_8)
        val dynamicView: DynamicView = workspace.views.createDynamicView(
            apiApplication,
            diagramName,
            "description",
            C4PlantUmlLayout(legend = Legend.SHOW_FLOATING_LEGEND)
        )
        addElements(dynamicView)

        val diagramFolder = File(tempDir, "./diagram/")
        C4PlantUmlDiagramWriter.writeDiagrams(
            diagramFolder,
            workspace
        )

        assertThat(diagramFolder.isDirectory).isTrue
        val actualDiagramFile = File(diagramFolder, "${dynamicView.key}.puml")
        assertThat(actualDiagramFile.isFile).isTrue
        assertThat(actualDiagramFile.readText(Charsets.UTF_8)).isEqualToIgnoringWhitespace(expectedDiagramContent)
    }

    @Test
    fun `test interaction diagram for system is written to plant uml as expected`() {
        val diagramName = "DynamicSystem"
        val expectedDiagramContent =
            this::class.java.getResource("/expected/$diagramName.puml")!!.readText(Charsets.UTF_8)
        val dynamicView: DynamicView = workspace.views.createDynamicView(
            system1,
            diagramName,
            "description",
            C4PlantUmlLayout(legend = Legend.SHOW_FLOATING_LEGEND)
        )
        dynamicView.add(singlePageApplication, "gets data from", apiApplication)
        dynamicView.add(apiApplication, "stores data to", database)

        val diagramFolder = File("./diagram/")
        C4PlantUmlDiagramWriter.writeDiagrams(
            diagramFolder,
            workspace
        )

        assertThat(diagramFolder.isDirectory).isTrue
        val actualDiagramFile = File(diagramFolder, "${dynamicView.key}.puml")
        assertThat(actualDiagramFile.isFile).isTrue
        assertThat(actualDiagramFile.readText(Charsets.UTF_8)).isEqualToIgnoringWhitespace(expectedDiagramContent)
    }
}
