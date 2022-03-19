package com.github.chriskn.structurizrextension

import com.github.chriskn.structurizrextension.model.C4Properties
import com.github.chriskn.structurizrextension.model.C4Type
import com.github.chriskn.structurizrextension.model.Dependency
import com.github.chriskn.structurizrextension.model.addContainer
import com.github.chriskn.structurizrextension.model.addDeploymentNode
import com.github.chriskn.structurizrextension.model.addSoftwareSystem
import com.github.chriskn.structurizrextension.model.icon
import com.github.chriskn.structurizrextension.model.type
import com.github.chriskn.structurizrextension.plantuml.C4PlantUmlDiagramWriter
import com.github.chriskn.structurizrextension.plantuml.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.plantuml.layout.Direction
import com.structurizr.Workspace
import com.structurizr.model.Container
import com.structurizr.model.Location
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class DeploymentViewTest {

    private val environment = "Production"

    @TempDir
    private lateinit var tempDir: File

    private val diagramName = "Deployment"

    private val expectedDiagramContent =
        this::class.java.getResource("/expected/$diagramName.puml")!!.readText(Charsets.UTF_8)

    @Test
    fun `deployment diagram is written as expected`() {
        val workspace = Workspace(
            "My deployment",
            "An example deployment description"
        )
        val model = workspace.model
        val views = workspace.views

        val mySystem = model.addSoftwareSystem(
            Location.Internal,
            "System container",
            "Example System"
        )
        val iosApp = model.addSoftwareSystem(
            location = Location.External,
            name = "iOS App",
        )
        val webApplication: Container = mySystem.addContainer(
            "Web Application",
            "Spring Boot web application",
            technology = "Java and Spring MVC",
            icon = "springboot"
        )
        val webAppSidecar: Container = mySystem.addContainer(
            "Sidecar",
            "Some sidecar"
        )
        val nginx: Container = mySystem.addContainer(
            name = "Load Balancer",
            description = "Nginx Load Balancer",
            technology = "nginx",
            icon = "nginx",
            link = "https://www.google.de",
            uses = listOf(Dependency(webApplication, "forwards requests to")),
            usedBy = listOf(Dependency(iosApp, "requests data from"))
        )
        val database: Container = mySystem.addContainer(
            "Database",
            "Stores data",
            technology = "PostgreSql",
            icon = "postgresql",
            type = C4Type.DATABASE,
            properties = C4Properties(values = listOf(listOf("region", "eu-central-1"))),
            usedBy = listOf(Dependency(webApplication, "stores data in", "JDBC"))
        )
        val failoverDatabase: Container = mySystem.addContainer(
            "Failover Database",
            database.description,
            technology = database.technology,
            icon = database.icon,
            type = database.type,
            properties = C4Properties(values = listOf(listOf("region", "eu-west-1"))),
            usedBy = listOf(Dependency(database, "replicates data to"))
        )
        val aws = model.addDeploymentNode(
            environment,
            "AWS",
            "Production AWS environment",
            icon = "aws", // TODO add
            properties = C4Properties(
                headers = listOf("Property", "Value", "Description"),
                values = listOf(
                    listOf("Property1", "Value1", "Description1"),
                    listOf("Property2", "Value2", "Description2"),
                )
            )
        )
        aws.addDeploymentNode(
            "AWS RDS",
            icon = "rds",
            hostsContainers = listOf(failoverDatabase, database)
        )
        aws.addDeploymentNode(
            "EKS cluster",
            icon = "awsEKSCloud",
            hostsContainers = listOf(nginx)
        ).addDeploymentNode(
            "my.web.app",
            "Web App POD",
            hostsContainers = listOf(webAppSidecar)
        ).addDeploymentNode(
            "Web App container",
            icon = "docker",
            hostsContainers = listOf(webApplication)
        )

        model.addDeploymentNode(
            environment = environment,
            "Apple Device",
            icon = "apple",
            hostsSystems = listOf(iosApp)
        )

        val deploymentView =
            views.createDeploymentView(
                mySystem,
                diagramName,
                "A deployment diagram showing the $environment environment.",
                C4PlantUmlLayout(direction = Direction.LEFT_TO_RIGHT, nodeSep = 20, rankSep = 50)
            )
        deploymentView.environment = environment
        deploymentView.addAllDeploymentNodes()

        val diagramFolder = File(tempDir, "./diagram/")
        C4PlantUmlDiagramWriter.writeDiagrams(
            diagramFolder,
            workspace
        )

        assertThat(diagramFolder.isDirectory).isTrue
        val actualDiagramFile = File(diagramFolder, "${deploymentView.key}.puml")
        assertThat(actualDiagramFile.isFile).isTrue
        assertThat(actualDiagramFile.readText(Charsets.UTF_8)).isEqualToIgnoringWhitespace(expectedDiagramContent)
    }
}
