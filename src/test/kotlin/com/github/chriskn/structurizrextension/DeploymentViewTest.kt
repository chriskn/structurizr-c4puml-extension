package com.github.chriskn.structurizrextension

import com.github.chriskn.structurizrextension.model.C4Properties
import com.github.chriskn.structurizrextension.model.C4Type
import com.github.chriskn.structurizrextension.model.Dependency
import com.github.chriskn.structurizrextension.model.container
import com.github.chriskn.structurizrextension.model.deploymentNode
import com.github.chriskn.structurizrextension.model.icon
import com.github.chriskn.structurizrextension.model.infrastructureNode
import com.github.chriskn.structurizrextension.model.softwareSystem
import com.github.chriskn.structurizrextension.model.type
import com.github.chriskn.structurizrextension.plantuml.C4PlantUmlDiagramWriter
import com.github.chriskn.structurizrextension.plantuml.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.plantuml.layout.DependencyConfiguration
import com.github.chriskn.structurizrextension.plantuml.layout.Direction
import com.github.chriskn.structurizrextension.plantuml.layout.Layout
import com.structurizr.Workspace
import com.structurizr.model.Container
import com.structurizr.model.Location
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class DeploymentViewTest {

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
        val iosApp = model.softwareSystem(
            location = Location.External,
            name = "iOS App",
            description = "iOS Application"
        )
        val webApplication: Container = mySystem.container(
            "Web Application",
            "Spring Boot web application",
            technology = "Java and Spring MVC",
            icon = "springboot"
        )
        val database: Container = mySystem.container(
            "Database",
            "Stores data",
            technology = "PostgreSql",
            icon = "postgresql",
            type = C4Type.DATABASE,
            properties = C4Properties(values = listOf(listOf("region", "eu-central-1"))),
            usedBy = listOf(Dependency(webApplication, "stores data in", "JDBC"))
        )
        val failoverDatabase: Container = mySystem.container(
            "Failover Database",
            database.description,
            technology = database.technology,
            icon = database.icon,
            type = database.type,
            properties = C4Properties(values = listOf(listOf("region", "eu-west-1"))),
            usedBy = listOf(Dependency(database, "replicates data to"))
        )
        val aws = model.deploymentNode(
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
        aws.deploymentNode(
            "AWS RDS",
            icon = "rds",
            hostsContainers = listOf(failoverDatabase, database)
        )
        val eks = aws.deploymentNode(
            "EKS cluster",
            icon = "awsEKSCloud",
        )

        val webAppPod = eks.deploymentNode(
            "my.web.app",
            "Web App POD"
        ).deploymentNode(
            "Web App container",
            icon = "docker",
            hostsContainers = listOf(webApplication)
        )
        webAppPod.infrastructureNode(
            "Sidecar",
            "Some sidecar"
        )
        val appleDevice = model.deploymentNode(
            "Apple Device",
            icon = "apple",
            hostsSystems = listOf(iosApp)
        )

        val loadBalancer = eks.infrastructureNode(
            name = "Load Balancer",
            description = "Nginx Load Balancer",
            technology = "nginx",
            icon = "nginx",
            link = "https://www.google.de",
            uses = listOf(Dependency(webAppPod, "forwards requests to")),
            usedBy = listOf(Dependency(appleDevice, "requests data from")),
            properties = C4Properties(
                headers = listOf("Property", "value"),
                values = listOf(listOf("IP", "10.234.234.132"))
            )
        )

        val deploymentView =
            views.deploymentView(
                mySystem,
                diagramName,
                "A deployment diagram showing the environment.",
                C4PlantUmlLayout(
                    nodeSep = 50,
                    rankSep = 50,
                    layout = Layout.LEFT_TO_RIGHT,
                    dependencyConfigurations =
                    listOf(
                        DependencyConfiguration(
                            filter = {
                                it.source == loadBalancer || it.destination.name == failoverDatabase.name
                            },
                            direction = Direction.RIGHT
                        )
                    )
                )
            )
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
