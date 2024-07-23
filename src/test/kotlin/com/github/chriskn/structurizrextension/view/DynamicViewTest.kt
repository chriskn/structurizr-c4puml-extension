package com.github.chriskn.structurizrextension.view

import com.github.chriskn.structurizrextension.api.model.C4Properties
import com.github.chriskn.structurizrextension.api.model.C4Type
import com.github.chriskn.structurizrextension.api.model.Dependency
import com.github.chriskn.structurizrextension.api.model.component
import com.github.chriskn.structurizrextension.api.model.container
import com.github.chriskn.structurizrextension.api.model.person
import com.github.chriskn.structurizrextension.api.model.softwareSystem
import com.github.chriskn.structurizrextension.api.view.dynamic.add
import com.github.chriskn.structurizrextension.api.view.dynamic.renderAsSequenceDiagram
import com.github.chriskn.structurizrextension.api.view.dynamic.startNestedParallelSequence
import com.github.chriskn.structurizrextension.api.view.dynamicView
import com.github.chriskn.structurizrextension.api.view.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.api.view.layout.DependencyConfiguration
import com.github.chriskn.structurizrextension.api.view.layout.Direction.Left
import com.github.chriskn.structurizrextension.api.view.layout.Direction.Right
import com.github.chriskn.structurizrextension.api.view.layout.Mode.Neighbor
import com.github.chriskn.structurizrextension.api.view.showExternalBoundaries
import com.github.chriskn.structurizrextension.assertExpectedDiagramWasWrittenForView
import com.structurizr.Workspace
import com.structurizr.model.InteractionStyle.Asynchronous
import com.structurizr.model.Model
import com.structurizr.view.DynamicView
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class DynamicViewTest {

    private val pathToExpectedDiagrams = "view/dynamic"

    @ParameterizedTest(name = "{index} asSequenceDiagram = {0}")
    @ValueSource(booleans = [true, false])
    fun `DynamicWithParallelFlow is written to plant uml as expected`(asSequenceDiagram: Boolean) {
        val diagramKey = determineDiagramPath("DynamicWithParallelFlow", asSequenceDiagram)

        val dynamicView: DynamicView = workspace.views.dynamicView(
            customerInformationSystem,
            diagramKey,
            "This diagram shows what happens when a customer updates their details"
        )

        configureWithParallelNumbering(dynamicView)
        dynamicView.renderAsSequenceDiagram = asSequenceDiagram

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @ParameterizedTest(name = "{index} asSequenceDiagram = {0}")
    @ValueSource(booleans = [true, false])
    fun `DynamicWithNestedParallelFlow is written to plant uml as expected`(asSequenceDiagram: Boolean) {
        val diagramKey = determineDiagramPath("DynamicWithNestedParallelFlow", asSequenceDiagram)
        val dynamicView: DynamicView = workspace.views.createDynamicView(
            customerInformationSystem,
            diagramKey,
            "This diagram shows what happens when a customer updates their details"
        )

        configureWithNestedParallelNumbering(dynamicView)
        dynamicView.renderAsSequenceDiagram = asSequenceDiagram

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @ParameterizedTest(name = "{index} asSequenceDiagram = {0}")
    @ValueSource(booleans = [true, false])
    fun `DynamicWithBoundary with boundaries is written to plant uml as expected`(asSequenceDiagram: Boolean) {
        val diagramKey = determineDiagramPath("DynamicWithBoundary", asSequenceDiagram)

        val dynamicView: DynamicView = workspace.views.dynamicView(
            reportingService,
            diagramKey,
            "This diagram shows what happens when a customer updates their details"
        )
        dynamicView.add(reportingController, reportingRepository, "Stores entity")

        dynamicView.showExternalBoundaries = true
        dynamicView.renderAsSequenceDiagram = asSequenceDiagram

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @ParameterizedTest(name = "{index} asSequenceDiagram = {0}")
    @ValueSource(booleans = [true, false])
    fun `DynamicWithLayout is written to plant uml as expected`(asSequenceDiagram: Boolean) {
        val diagramKey = determineDiagramPath("DynamicWithLayout", asSequenceDiagram)
        val dynamicView: DynamicView = workspace.views.dynamicView(
            customerInformationSystem,
            diagramKey,
            "This diagram shows what happens when a customer updates their details",
            layout = C4PlantUmlLayout(
                dependencyConfigurations = listOf(
                    DependencyConfiguration(
                        filter = { it.source == customerFrontend || it.destination == messageBus },
                        direction = Right
                    ),
                    DependencyConfiguration(
                        filter = { it.source == customerService && it.destination == customerFrontend },
                        direction = Left
                    )
                )
            )
        )

        configureWithNestedParallelNumbering(dynamicView)
        dynamicView.renderAsSequenceDiagram = asSequenceDiagram

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `parallelSequence does not throw Exception if no steps added`() {
        val dynamicView: DynamicView = workspace.views.dynamicView(customerInformationSystem, "some_key", "desc")

        assertDoesNotThrow {
            dynamicView
                .startNestedParallelSequence()
                .endParallelSequence()
        }
    }

    @Test
    fun `setting the mode for dependencies in dynamic views is ignored`() {
        val diagramKey = "DynamicView"
        val dynamicView: DynamicView = workspace.views.dynamicView(
            customerInformationSystem,
            diagramKey,
            "desc",
            layout = C4PlantUmlLayout(
                dependencyConfigurations = listOf(
                    DependencyConfiguration(
                        filter = { it.source == customer },
                        mode = Neighbor
                    )
                )
            )
        )

        configureWithNestedParallelNumbering(dynamicView)

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `NestedParallelSequence starts with 1 dot 1`() {
        val dynamicView: DynamicView = workspace.views.dynamicView(customerInformationSystem, "key", "desc")

        val parallelSequence = dynamicView.startNestedParallelSequence()
        parallelSequence.add(customer, customerFrontend, "Uses")
        parallelSequence.endParallelSequence()

        assertThat(dynamicView.relationships).hasSize(1)
        assertThat(dynamicView.relationships.last().order).isEqualTo("1.1")
    }

    @Test
    fun `Sequence diagram is ordered correctly`() {
        val diagramKey = "DynamicViewSequenceOrdering"

        val admin = model.person(
            name = "Admin",
            description = "A System admin",
        )
        val customer = model.person(
            name = "Admin Customer",
            description = "An customer",
            uses = listOf(
                Dependency(
                    destination = admin,
                    description = "uses"
                )
            )
        )

        val dynamicView = workspace.views.dynamicView(
            customerInformationSystem,
            diagramKey,
            "More than 10 messages",
        )
        (1..7).map {
            dynamicView.add(
                source = customer,
                destination = admin,
                description = "Step $it request"
            )
            dynamicView.add(
                source = admin,
                destination = customer,
                description = "Step $it response"
            )
        }

        dynamicView.renderAsSequenceDiagram = true

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    private fun determineDiagramPath(base: String, asSequenceDiagram: Boolean) = if (!asSequenceDiagram) {
        base
    } else {
        "${base}Sequence"
    }

    private fun configureWithNestedParallelNumbering(dynamicView: DynamicView) {
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
        dynamicView.add(customerFrontend, customer, "Sends feedback to")
    }

    private fun configureWithParallelNumbering(dynamicView: DynamicView) {
        dynamicView.add(customer, customerFrontend, "Uses")
        dynamicView.add(customerFrontend, customerService, "Updates customer information using")

        dynamicView.add(customerService, customerDatabase, "Stores data in")
        dynamicView.add(customerService, messageBus, "Sends customer update events to")

        dynamicView.startParallelSequence()
        dynamicView.add(messageBus, reportingService, "Sends customer update events to")
        dynamicView.add(reportingService, reportingDatabase, "Stores data in")
        dynamicView.endParallelSequence()

        dynamicView.startParallelSequence()
        dynamicView.add(messageBus, auditingService, "Sends customer update events to")
        dynamicView.add(auditingService, auditStore, "Stores events in")
        dynamicView.endParallelSequence()

        dynamicView.startParallelSequence()
        dynamicView.add(customerService, customerFrontend, "Confirms update to")
        dynamicView.endParallelSequence()
    }

    private var workspace = Workspace("My Workspace", "Some Description")
    private var model: Model = workspace.model
    private val customer = model.person("Customer", "A costumer")
    private val customerInformationSystem = model.softwareSystem(
        "Customer Information System",
        "",
        usedBy = listOf(Dependency(customer, ""))
    )
    private val customerFrontend = customerInformationSystem.container(
        "Customer Frontend Application",
        "Allows customers to manage their profile",
        technology = "Angular",
        usedBy = listOf(Dependency(customer, ""))
    )
    private val customerService = customerInformationSystem.container(
        "Customer Service",
        "The point of access for customer information.",
        technology = "Java and Spring Boot",
        usedBy = listOf(Dependency(customerFrontend, "", technology = "JSON/HTTPS")),
        uses = listOf(
            Dependency(
                customerFrontend,
                "",
                interactionStyle = Asynchronous,
                technology = "WebSocket",
                link = "www.bing.com"
            )
        )
    )
    private val customerDatabase = customerInformationSystem.container(
        "Customer Database",
        "Stores customer information",
        technology = "Oracle",
        c4Type = C4Type.DATABASE,
        usedBy = listOf(Dependency(customerService, "", technology = "JDBC", link = "www.google.com"))
    )
    private val messageBus = customerInformationSystem.container(
        "Message Bus",
        "Transport for business events.",
        technology = "RabbitMQ",
        c4Type = C4Type.QUEUE,
        usedBy = listOf(
            Dependency(
                customerService,
                "",
                interactionStyle = Asynchronous,
                properties = C4Properties(
                    header = listOf("field", "value"),
                    values = listOf(listOf("Event Type", "create"))
                )
            )
        )
    )
    private val reportingService = customerInformationSystem.container(
        "Reporting Service",
        "Creates normalised data for reporting purposes.",
        technology = "Ruby",
        usedBy = listOf(Dependency(messageBus, "", interactionStyle = Asynchronous))
    )
    private val reportingDatabase = customerInformationSystem.container(
        "Reporting Database",
        "Stores a normalised version of all business data for ad hoc reporting purposes",
        technology = "MySql",
        c4Type = C4Type.DATABASE,
        usedBy = listOf(Dependency(reportingService, "", icon = "mysql"))
    )
    private val auditingService = customerInformationSystem.container(
        "Auditing Service",
        "Provides organisation-wide auditing facilities.",
        technology = "C#, Net",
        usedBy = listOf(Dependency(messageBus, "", interactionStyle = Asynchronous)),
    )
    private val auditStore = customerInformationSystem.container(
        "Audit Store",
        "Stores information about events that have happened",
        technology = "Event Store",
        c4Type = C4Type.DATABASE,
        usedBy = listOf(Dependency(auditingService, ""))
    )
    private val reportingController = reportingService.component(
        "Reporting Controller",
        "Reporting Controller",
        technology = "REST"
    )
    private val reportingRepository = reportingService.component(
        "Reporting Repository",
        "Reporting repository",
        technology = "JDBC",
        usedBy = listOf(Dependency(reportingController, ""))
    )
}
