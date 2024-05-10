package com.github.chriskn.structurizrextension.view

import com.github.chriskn.structurizrextension.assertExpectedDiagramWasWrittenForView
import com.github.chriskn.structurizrextension.model.C4Properties
import com.github.chriskn.structurizrextension.model.Dependency
import com.github.chriskn.structurizrextension.model.person
import com.github.chriskn.structurizrextension.model.softwareSystem
import com.github.chriskn.structurizrextension.plantuml.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.plantuml.Layout
import com.github.chriskn.structurizrextension.plantuml.Legend
import com.structurizr.Workspace
import com.structurizr.model.Location
import com.structurizr.model.Model
import com.structurizr.model.enterprise
import com.structurizr.view.showEnterpriseBoundary
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class ContextViewTest {

    private val pathToExpectedDiagrams = "view/context"

    private val workspace = Workspace("My Workspace", "Some Description")
    private val model: Model = workspace.model

    private val system1 = model.softwareSystem(
        "Software System 1",
        "Description 1",
        icon = "android",
        link = "https://www.android.com",
        tags = listOf("tag1", "tag2")
    )

    @BeforeAll
    fun setUpModel() {
        model.enterprise("My Enterprise")
        val system0 = model.softwareSystem(
            "Software System 0",
            "Description 0",
            Location.External,
            tags = listOf("tag1", "tag2"),
            uses = listOf(Dependency(system1, "0 used by 1"))
        )
        model.softwareSystem(
            "Software System 2",
            "Description 2",
            Location.Internal,
            icon = "docker",
            link = "https://www.docker.com/",
            properties = C4Properties(
                header = listOf("Property", "Value"),
                values = listOf(
                    listOf("prop key0", "prop value0"),
                    listOf("prop key1", "prop value1"),
                    listOf("prop key2", "prop value2")
                )
            ),
            uses = listOf(
                Dependency(system0, "2 uses 1")
            )
        )
        model.person(
            "Actor",
            link = "https://www.google.de",
            tags = listOf("human"),
            uses = listOf(
                Dependency(
                    system1,
                    "creates",
                    "HTTP",
                    link = "https://de.wikipedia.org/wiki/Hypertext_Transfer_Protocol",
                    icon = "html5",
                    properties = C4Properties(
                        values = listOf(
                            listOf("prop", "val")
                        )
                    )
                ),
                Dependency(system0, "deletes", "gRPC")
            ),
            properties = C4Properties(values = listOf(listOf("prop 1", "value 1")))
        )
    }

    @Test
    fun `landscape diagram is written as expected`() {
        val diagramKey = "SystemLandscape"
        val landscapeView = workspace.views.systemLandscapeView(
            diagramKey,
            "A test Landscape",
            C4PlantUmlLayout(layout = Layout.LeftToRight, legend = Legend.ShowFloatingLegend)
        )
        landscapeView.addAllElements()
        landscapeView.showEnterpriseBoundary = true

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }

    @Test
    fun `context diagram is written as expected`() {
        val diagramKey = "Context"
        val contextView = workspace.views.systemContextView(
            system1,
            diagramKey,
            "A test Landscape",
            C4PlantUmlLayout(
                layout = Layout.LeftToRight,
                legend = Legend.ShowFloatingLegend,
                hideStereotypes = false
            )
        )
        contextView.addDefaultElements()

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }
}
