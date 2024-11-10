package com.github.chriskn.structurizrextension.view

import com.github.chriskn.structurizrextension.api.model.C4Properties
import com.github.chriskn.structurizrextension.api.model.C4Type
import com.github.chriskn.structurizrextension.api.model.Dependency
import com.github.chriskn.structurizrextension.api.model.c4Type
import com.github.chriskn.structurizrextension.api.model.container
import com.github.chriskn.structurizrextension.api.model.deploymentNode
import com.github.chriskn.structurizrextension.api.model.infrastructureNode
import com.github.chriskn.structurizrextension.api.model.softwareSystem
import com.github.chriskn.structurizrextension.api.model.sprite
import com.github.chriskn.structurizrextension.api.view.deploymentView
import com.github.chriskn.structurizrextension.api.view.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.api.view.layout.DependencyConfiguration
import com.github.chriskn.structurizrextension.api.view.layout.Direction.Right
import com.github.chriskn.structurizrextension.api.view.layout.Layout.LeftToRight
import com.github.chriskn.structurizrextension.api.view.sprite.registry.SpriteRegistry
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
            sprite = SpriteRegistry.spriteByName("logos-spring"),
        )
        val database: Container = mySystem.container(
            "Database",
            "Stores data",
            technology = "PostgreSql",
            sprite = SpriteRegistry.spriteByName("logos-postgresql"),
            c4Type = C4Type.DATABASE,
            properties = C4Properties(values = listOf(listOf("region", "eu-central-1"))),
            usedBy = listOf(Dependency(webApplication, "stores data in", "JDBC"))
        )
        val failoverDatabase: Container = mySystem.container(
            name = "Failover Database",
            description = database.description,
            technology = database.technology,
            sprite = database.sprite,
            c4Type = database.c4Type,
            properties = C4Properties(values = listOf(listOf("region", "eu-west-1"))),
            usedBy = listOf(Dependency(database, "replicates data to"))
        )
        val aws = model.deploymentNode(
            "AWS",
            "Production AWS environment",
            sprite = SpriteRegistry.spriteByName("aws-Groups-AWSCloudAlt"),
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
            sprite = SpriteRegistry.spriteByName("aws-database-AuroraPostgreSQLInstance"),
            hostsContainers = listOf(failoverDatabase, database)
        )
        val eks = aws.deploymentNode(
            "EKS cluster",
            sprite = SpriteRegistry.spriteByName("aws-containers-EKSCloud"),
        )

        val webAppPod = eks.deploymentNode(
            "my.web.app",
            "Web App POD"
        ).deploymentNode(
            "Web App container",
            sprite = SpriteRegistry.spriteByName("logos-docker-icon"),
            hostsContainers = listOf(webApplication)
        )
        val jaegerSidecar = webAppPod.infrastructureNode(
            "Jaeger Sidecar",
            "Jaeger sidecar sending Traces",
            sprite = SpriteRegistry.spriteByName("tupadr3-devicons2-jaegertracing")
        )
        model.deploymentNode(
            "Another AWS Account",
            sprite = SpriteRegistry.spriteByName("aws-groups-AWSCloudAlt")
        ).deploymentNode(
            "Jaeger Container",
            usedBy = listOf(
                Dependency(
                    jaegerSidecar,
                    "writes traces to",
                    interactionStyle = Asynchronous,
                    sprite = SpriteRegistry.spriteByName("logos-kafka-icon"),
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
            sprite = SpriteRegistry.spriteByName("tupadr3-devicons-apple"),
            hostsSystems = listOf(iosApp)
        )

        val loadBalancer = eks.infrastructureNode(
            name = "Load Balancer",
            description = "Nginx Load Balancer",
            technology = "nginx",
            sprite = SpriteRegistry.spriteByName("logos-nginx"),
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
