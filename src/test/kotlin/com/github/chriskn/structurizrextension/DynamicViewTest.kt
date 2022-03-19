package com.github.chriskn.structurizrextension

import com.github.chriskn.structurizrextension.model.C4Type
import com.github.chriskn.structurizrextension.model.Dependency
import com.github.chriskn.structurizrextension.model.addComponent
import com.github.chriskn.structurizrextension.model.addContainer
import com.github.chriskn.structurizrextension.model.addSoftwareSystem
import com.github.chriskn.structurizrextension.plantuml.C4PlantUmlDiagramWriter
import com.github.chriskn.structurizrextension.plantuml.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.plantuml.layout.Legend
import com.structurizr.Workspace
import com.structurizr.model.Container
import com.structurizr.model.Enterprise
import com.structurizr.view.DynamicView
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class DynamicViewTest {

    @TempDir
    private lateinit var tempDir: File

    private val diagramName = "Dynamic"

    private val expectedDiagramContent =
        this::class.java.getResource("/expected/$diagramName.puml")!!.readText(Charsets.UTF_8)

    @Test
    fun `test interaction diagram is written to plant uml as expected`() {
        val workspace = Workspace("My Workspace", "Some Description")
        val model = workspace.model
        model.enterprise = Enterprise("My Enterprise")
        val system1 = model.addSoftwareSystem(
            name = "Software System 1",
            description = "Description 1",
        )
        val singlePageApplication: Container = system1.addContainer(
            "Single-Page Application",
            "Provides all of the Internet banking functionality to customers via their web browser.",
            technology = "JavaScript and Angular",
            icon = "Docker"
        )
        val apiApplication: Container = system1.addContainer(
            "API Application",
            "Provides Internet banking functionality via a JSON/HTTPS API.",
            technology = "Java and Spring MVC"
        )
        val database: Container = system1.addContainer(
            "Database",
            "Stores user registration information, hashed authentication credentials, access logs, etc.",
            technology = "Oracle Database Schema",
            type = C4Type.DATABASE
        )
        val loginController = apiApplication.addComponent(
            "Sign In Controller",
            "Allows users to sign in to the Internet Banking System.",
            technology = "Spring MVC Rest Controller",
            usedBy = listOf(
                Dependency(singlePageApplication, "")
            )
        )
        val securityComponent = apiApplication.addComponent(
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
        val dynamicView: DynamicView = workspace.views.createDynamicView(
            apiApplication,
            diagramName,
            "description",
            C4PlantUmlLayout(legend = Legend.SHOW_FLOATING_LEGEND)
        )
        dynamicView.add(singlePageApplication, "Submits credentials to", loginController)
        dynamicView.add(loginController, "Validates credentials using", securityComponent)
        dynamicView.add(securityComponent, "select * from users where username = ?", database)
        dynamicView.add(database, "Returns user data to", securityComponent)
        dynamicView.add(securityComponent, "Returns true if the hashed password matches", loginController)
        dynamicView.add(loginController, "Sends back an authentication token to", singlePageApplication)

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
}
