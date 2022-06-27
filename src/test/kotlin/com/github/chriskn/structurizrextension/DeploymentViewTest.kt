package com.github.chriskn.structurizrextension

import com.github.chriskn.structurizrextension.model.C4Properties
import com.github.chriskn.structurizrextension.model.C4Type
import com.github.chriskn.structurizrextension.model.Dependency
import com.github.chriskn.structurizrextension.model.c4Type
import com.github.chriskn.structurizrextension.model.container
import com.github.chriskn.structurizrextension.model.deploymentNode
import com.github.chriskn.structurizrextension.model.icon
import com.github.chriskn.structurizrextension.model.infrastructureNode
import com.github.chriskn.structurizrextension.model.softwareSystem
import com.github.chriskn.structurizrextension.plantuml.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.plantuml.layout.DependencyConfiguration
import com.github.chriskn.structurizrextension.plantuml.layout.Direction
import com.github.chriskn.structurizrextension.plantuml.layout.Layout
import com.structurizr.Workspace
import com.structurizr.model.Container
import com.structurizr.model.InteractionStyle
import com.structurizr.model.Location
import org.junit.jupiter.api.Test

class DeploymentViewTest {

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
            c4Type = C4Type.DATABASE,
            properties = C4Properties(values = listOf(listOf("region", "eu-central-1"))),
            usedBy = listOf(Dependency(webApplication, "stores data in", "JDBC"))
        )
        val failoverDatabase: Container = mySystem.container(
            "Failover Database",
            database.description,
            technology = database.technology,
            icon = database.icon,
            c4Type = database.c4Type,
            properties = C4Properties(values = listOf(listOf("region", "eu-west-1"))),
            usedBy = listOf(Dependency(database, "replicates data to"))
        )
        val aws = model.deploymentNode(
            "AWS",
            "Production AWS environment",
            icon = "aws",
            properties = C4Properties(
                header = listOf("Property", "Value", "Description"),
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
        val jaegerSidecar = webAppPod.infrastructureNode(
            "Jaeger Sidecar",
            "Jaeger sidecar sending Traces"
        )
        model.deploymentNode(
            "Another AWS Account",
            icon = "aws"
        ).deploymentNode(
            "Jaeger Container",
            usedBy = listOf(
                Dependency(
                    jaegerSidecar,
                    "writes traces to",
                    interactionStyle = InteractionStyle.Asynchronous,
                    icon = "kafka",
                    link = "https://www.jaegertracing.io/",
                    properties = C4Properties(
                        header = listOf("key", "value"),
                        values = listOf(listOf("ip", "10.234.12.13"))
                    )
                )
            )
        ).infrastructureNode("Jaeger")
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
                header = listOf("Property", "value"),
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
                    layout = Layout.LeftToRight,
                    dependencyConfigurations =
                    listOf(
                        DependencyConfiguration(
                            filter = {
                                it.source == loadBalancer || it.destination.name == failoverDatabase.name
                            },
                            direction = Direction.Right
                        )
                    )
                )
            )
        deploymentView.addDefaultElements()

        assertExpectedDiagramWasWrittenForView(workspace, deploymentView.key, expectedDiagramContent)
    }
}
