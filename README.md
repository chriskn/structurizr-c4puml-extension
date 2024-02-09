[![PR pipeline](https://github.com/chriskn/structurizr-c4puml-extension/actions/workflows/pr-pipeline.yml/badge.svg?branch=main)](https://github.com/chriskn/structurizr-c4puml-extension/actions/workflows/pr-pipeline.yml)

# Structurizr C4-PlantUML extension

Structurizr C4-PlantUML extension aims to bridge the gap between the [structurizr java library](https://github.com/structurizr/java) and [C4-PlantUML](https://github.com/plantuml-stdlib/C4-PlantUML) by extending the structurizr model and providing an extended C4-PlantUML exporter. It is written in Kotlin.   

## Table of contents
  * [Examples](#example)
  * [How to use it](#how-to-use-it)
  * [Related resources](#related-resources)

## Examples

The following examples diagrams demonstrate the additional features the Structurizr C4-PlantUML extension provides: 

* links, icons and properties for elements and relationships
* external containers 
* visualization for system and container boundaries
* database and queue shapes
* differentiation between synchronous and asynchronous relationships
* nested numbered parallel sequences for dynamic diagrams
* advanced layout configuration for C4-PlantUML diagrams

More examples can be found under `src/test/kotlin`

### Core diagrams 

![Example container diagram](./docs/container_example.svg)

The following code creates and writes the example container diagram:

```kotlin
val workspace = Workspace("My Workspace", "")
val model = workspace.model
val properties = C4Properties(values = listOf(listOf("prop 1", "value 1")))
val softwareSystem = model.softwareSystem(
    name = "My Software System",
    description = "system description",
    link = "https://www.google.de"
)
val backendApplication = softwareSystem.container(
    name = "Backend App",
    description = "some backend app",
    technology = "Kotlin",
    tags = listOf("Tag2"),
    icon = "docker",
    link = "https://www.google.de",
    properties = properties
)
val app = softwareSystem.container(
    name = "App",
    description = "android app",
    technology = "Android",
    icon = "android",
)
val database = softwareSystem.container(
    name = "Database",
    description = "some database",
    c4Type = C4Type.DATABASE,
    technology = "postgres",
    icon = "postgresql",
    usedBy = listOf(Dependency(backendApplication, "CRUD", "JPA"))
)
val broker = model.softwareSystem(
    name = "Broker",
    description = "Message Broker",
    location = Location.External,
    c4Type = C4Type.QUEUE,
    icon = "kafka",
)
val topic = broker.container(
    "my.topic",
    "external topic",
    c4Type = C4Type.QUEUE,
    icon = "kafka",
    usedBy = listOf(
        Dependency(backendApplication, "reads topic", "Avro", interactionStyle = InteractionStyle.Asynchronous)
    )
)
val graphql = model.softwareSystem(
    name = "GraphQL",
    description = "Federated GraphQL",
    location = Location.External,
    icon = "graphql"
)
val internalSchema = graphql.container(
    name = "Internal Schema",
    description = "Schema provided by our app",
    location = Location.Internal,
    usedBy = listOf(
        Dependency(backendApplication, "provides subgraph to"),
        Dependency(app, "reuqest data using", "GraphQL", icon = "graphql", link = "https://graphql.org/")
    )
)
val externalSchema = graphql.container(
    name = "External Schema",
    description = "Schema provided by another team",
    uses = listOf(Dependency(internalSchema, "extends schema"))
)
val androidUser = model.person(
    name = "Android User",
    description = "some Android user",
    location = Location.External,
    icon = "android",
    uses = listOf(Dependency(app, "uses app"))
)
val maintainer = model.person(
    name = "Maintainer",
    description = "some employee",
    location = Location.Internal,
    link = "https://www.google.de",
    uses = listOf(
        Dependency(backendApplication, "Admin UI", "REST")
    ),
    properties = properties
)

fun createAndWriteContainerView(){
    val containerView = workspace.views.containerView(
        softwareSystem,
        "ContainerWithBoundary",
        "Example container view",
        C4PlantUmlLayout(
            legend = Legend.ShowLegend,
            layout = Layout.TopDown,
            lineType = LineType.Ortho,
            nodeSep = 100,
            rankSep = 130,
            dependencyConfigurations = listOf(
                DependencyConfiguration(filter = { it.destination == database }, direction = Direction.Right),
                DependencyConfiguration(filter = { it.destination == topic }, direction = Direction.Up)
            )
        )
    )
    containerView.addAllContainers()
    containerView.externalSoftwareSystemBoundariesVisible = true
    containerView.add(topic)
    containerView.add(internalSchema)
    containerView.add(externalSchema)
    containerView.addDependentSoftwareSystems()
    containerView.addAllPeople()

    workspace.writeDiagrams(File("diagrams/"))
}
```
### Dynamic diagrams

As the following example shows, the C4-PlantUML extension provides, in addition to the parallel sequences provided by the Structurizr library, nested numbered parallel sequences for dynamic diagrams. 

![Example dynamic diagram](./docs/dynamic_example_nested.svg)

```kotlin
dynamicView.add(customer, customerFrontend, "Uses")
dynamicView.add(customerFrontend, customerService, "Updates customer information using")
dynamicView.add(customerService, customerDatabase, "Stores data in")
dynamicView.add(customerService, messageBus, "Sends customer update events to")
with(dynamicView.startNestedParallelSequence()) {
    add(messageBus, reportingService, "Sends customer update events to")
    with(this.startNestedParallelSequence()) {
        add(reportingService, reportingDatabase, "Stores data in")
        endParallelSequence()
    }
    add(messageBus, auditingService, "Sends customer update events to")
    with(this.startNestedParallelSequence()) {
        add(auditingService, auditStore, "Stores events in")
        endParallelSequence()
    }
    add(customerService, customerFrontend, "Confirms update to")
    endParallelSequence()
}
```

Dynamic diagrams can also be rendered as sequence diagram by setting the property `DynamicView.renderAsSequenceDiagram` to true.

![Example dynamic sequence diagram](./docs/dynamic_example_nested_as_sequence.svg)

### Deployment diagrams

As the following example demonstrates how to model deployments and create deployment diagrams using the C4-PlantUML extension.

![Example deployment diagram](./docs/deployment_example.svg)

```kotlin
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
            interactionStyle = InteractionStyle.Asynchronous,
            link = "https://www.jaegertracing.io/",
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

val deploymentView = views.deploymentView(
        mySystem,
        "Deployment",
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
                    direction = Direction.Left
                )
            )
        )
    )
deploymentView.addDefaultElements()
```

## How to use it 

Structurizr C4-PlantUML extension is available in maven central. 

Example using gradle kotlin:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.chriskn:structurizr-c4puml-extension:$currentVersion")
} 
```

## Related resources

### Blog posts

* [Part 1: Workflow and tooling](https://blog.codecentric.de/en/2022/08/architecture-documentation-docs-as-code-structurizr-asciidoctor)
* [Part 2: Asciidoctor](https://blog.codecentric.de/architecture-documentation-as-code-with-structurizr-and-asciidoctor-part-2-asciidoctor)
* [Part 3: Structurizr](https://blog.codecentric.de/architecture-documentation-as-code-with-structurizr-and-asciidoctor-part-3-structurizr)
* [Part 4: Publishing](https://blog.codecentric.de/architecture-documentation-as-code-with-structurizr-and-asciidoctor-part4-publishing)
* [Part 5: Generating documentation](https://blog.codecentric.de/architecture-docs-as-code-structurizr-asciidoctor-part-5-generating-documentation)

### Example project

An example project using this library can be found [on GitHub](https://github.com/chriskn/arch-docs-as-code-example/).
