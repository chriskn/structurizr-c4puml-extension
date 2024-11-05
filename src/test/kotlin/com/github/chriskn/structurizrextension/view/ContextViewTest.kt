package com.github.chriskn.structurizrextension.view

import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.api.model.C4Properties
import com.github.chriskn.structurizrextension.api.model.Dependency
import com.github.chriskn.structurizrextension.api.model.enterpriseName
import com.github.chriskn.structurizrextension.api.model.person
import com.github.chriskn.structurizrextension.api.model.softwareSystem
import com.github.chriskn.structurizrextension.api.view.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.api.view.layout.Layout.LeftToRight
import com.github.chriskn.structurizrextension.api.view.layout.Legend.ShowFloatingLegend
import com.github.chriskn.structurizrextension.api.view.showEnterpriseBoundary
import com.github.chriskn.structurizrextension.api.view.systemContextView
import com.github.chriskn.structurizrextension.api.view.systemLandscapeView
import com.github.chriskn.structurizrextension.assertExpectedDiagramWasWrittenForView
import com.structurizr.Workspace
import com.structurizr.model.Location
import com.structurizr.model.Model
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ContextViewTest {

    private val pathToExpectedDiagrams = "view/context"

    private val workspace = Workspace("My Workspace", "Some Description")
    private val model: Model = workspace.model

    private val system1 = model.softwareSystem(
        "Software System 1",
        "Description 1",
        sprite = IconRegistry.spriteForName("android"),
        link = "https://www.android.com",
        location = Location.Internal
    )

    @BeforeAll
    fun setUpModel() {
        model.enterpriseName = "My Enterprise"
        val system0 = model.softwareSystem(
            "Software System 0",
            "Description 0",
            Location.External,
            uses = listOf(Dependency(system1, "0 used by 1"))
        )
        model.softwareSystem(
            "Software System 2",
            "Description 2",
            sprite = IconRegistry.spriteForName("docker"),
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
            uses = listOf(
                Dependency(
                    system1,
                    "creates",
                    "HTTP",
                    link = "https://de.wikipedia.org/wiki/Hypertext_Transfer_Protocol",
                    sprite = IconRegistry.spriteForName("html5"),
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

    @Nested
    inner class Landscape {

        @Test
        fun `landscape diagram is written as expected with enterprise boundaries`() {
            val diagramKey = "SystemLandscapeWithBoundaries"
            val landscapeView = workspace.views.systemLandscapeView(
                diagramKey,
                "A test Landscape",
                C4PlantUmlLayout(layout = LeftToRight, legend = ShowFloatingLegend)
            )
            landscapeView.addAllElements()
            landscapeView.showEnterpriseBoundary = true

            assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
        }

        @Test
        fun `landscape diagram is written as expected without enterprise boundaries`() {
            val diagramKey = "SystemLandscapeWithoutBoundaries"
            val landscapeView = workspace.views.systemLandscapeView(
                diagramKey,
                "A test Landscape"
            )
            landscapeView.addAllElements()

            assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
        }

        @Test
        fun `landscape diagram is written as expected without enterprise boundaries if showEnterpriseBoundary is not set`() {
            val diagramKey = "SystemLandscapeDefault"
            val landscapeView = workspace.views.systemLandscapeView(
                diagramKey,
                "A test Landscape"
            )
            landscapeView.addAllElements()

            assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
        }
    }

    @Nested
    inner class Context {

        @Test
        fun `context diagram is written with boundaries`() {
            val diagramKey = "ContextWithBoundary"
            val contextView = workspace.views.systemContextView(
                system1,
                diagramKey,
                "A test Landscape",
                C4PlantUmlLayout(
                    layout = LeftToRight,
                    legend = ShowFloatingLegend,
                    hideStereotypes = false
                )
            )
            contextView.addDefaultElements()
            contextView.showEnterpriseBoundary = true

            assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
        }

        @Test
        fun `context diagram is written with boundaries by default as expected`() {
            val diagramKey = "ContextDefault"
            val contextView = workspace.views.systemContextView(
                system1,
                diagramKey,
                "A test Landscape",
                C4PlantUmlLayout(
                    layout = LeftToRight,
                    legend = ShowFloatingLegend,
                    hideStereotypes = false
                )
            )
            contextView.addDefaultElements()

            assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
        }
    }
}
