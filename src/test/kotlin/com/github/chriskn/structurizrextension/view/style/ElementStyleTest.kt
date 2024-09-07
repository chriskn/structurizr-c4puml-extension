package com.github.chriskn.structurizrextension.view.style

import com.github.chriskn.structurizrextension.api.model.softwareSystem
import com.github.chriskn.structurizrextension.api.view.style.C4Shape.EIGHT_SIDED
import com.github.chriskn.structurizrextension.api.view.style.Sprite
import com.github.chriskn.structurizrextension.api.view.style.addElementStyle
import com.github.chriskn.structurizrextension.api.view.style.createElementStyle
import com.github.chriskn.structurizrextension.api.view.systemContextView
import com.github.chriskn.structurizrextension.assertExpectedDiagramWasWrittenForView
import com.structurizr.Workspace
import com.structurizr.view.Border.Dashed
import org.junit.jupiter.api.Test
import java.net.URI

class ElementStyleTest {

    private val pathToExpectedDiagrams = "view/style"

    private val workspace = Workspace("My Workspace", "")
    private val model = workspace.model
    private val views = workspace.views

    @Test
    fun `element style is applied for software systems`() {
        val diagramKey = "BasicStyleTest"

        val styleTag = "System Style"

        val system = model.softwareSystem("My Software System", "system", tags = listOf(styleTag))
        val contextView = workspace.views.systemContextView(system, diagramKey, "A test Landscape",)

        val sprite = Sprite(URI.create("img:https://plantuml.com/logo3.png"), 0.4)
        val legendSprite = Sprite("compass", 3.0)
        val elementStyle = createElementStyle(
            tag = styleTag,
            backgroundColor = "#000000",
            border = Dashed,
            borderWith = 4,
            borderColor = "green",
            fontColor = "yellow",
            shadowing = true,
            technology = "Kafka",
            c4Shape = EIGHT_SIDED,
            sprite = sprite,
            legendSprite = legendSprite,
            legendText = "this is a legend"
        )
        views.addElementStyle(elementStyle)

        contextView.addDefaultElements()

        assertExpectedDiagramWasWrittenForView(workspace, pathToExpectedDiagrams, diagramKey)
    }
}
