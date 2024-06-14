package com.github.chriskn.structurizrextension.view

import com.github.chriskn.structurizrextension.api.model.C4Properties
import com.github.chriskn.structurizrextension.api.model.C4Type
import com.github.chriskn.structurizrextension.api.model.Dependency
import com.github.chriskn.structurizrextension.api.model.c4Type
import com.github.chriskn.structurizrextension.api.model.container
import com.github.chriskn.structurizrextension.api.model.deploymentNode
import com.github.chriskn.structurizrextension.api.model.icon
import com.github.chriskn.structurizrextension.api.model.infrastructureNode
import com.github.chriskn.structurizrextension.api.model.softwareSystem
import com.github.chriskn.structurizrextension.api.view.deploymentView
import com.github.chriskn.structurizrextension.api.view.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.api.view.layout.DependencyConfiguration
import com.github.chriskn.structurizrextension.api.view.layout.Direction.Right
import com.github.chriskn.structurizrextension.api.view.layout.Layout.LeftToRight
import com.github.chriskn.structurizrextension.assertExpectedDiagramWasWrittenForView
import com.structurizr.Workspace
import com.structurizr.model.Container
import com.structurizr.model.InteractionStyle.Asynchronous
import com.structurizr.model.Location
import org.junit.jupiter.api.Test

class DeploymentViewTest {

    private val pathToExpectedDiagrams = "view/deployment"
    private val diagramKey = "Deployment"

    @Test
    fun `deployment diagram is written as expected`() {
        val workspace = Workspace(
            "My deployment",
            "An example deployment description"
        )
        val model = workspace.model
        val views = workspace.views

        val mySystem = model.softwareSystem(
            "System container",
            "Example System",
            Location.Internal
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
                    interactionStyle = Asynchronous,
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
                diagramKey,
                "A deployment diagram showing the environment.",
                C4PlantUmlLayout(
                    nodeSep = 50,
                    rankSep = 50,
                    layout = LeftToRight,
                    dependencyConfigurations =
                    listOf(
                        DependencyConfiguration(
                            filter = {
                                it.source == loadBalancer || it.destination.name == failoverDatabase.name
                            },
                            direction = Right
                        )
                    )
                )
            )
        deploymentView.addDefaultElements()

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }
}
