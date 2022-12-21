package com.github.chriskn.structurizrextension.model

import com.github.chriskn.structurizrextension.assertExpectedDiagramWasWrittenForView
import com.github.chriskn.structurizrextension.view.systemContextView
import com.structurizr.Workspace
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class C4PropertiesTest {

    @Test
    fun `throws exception if num rows greater max rows`() {
        assertThrows<IllegalArgumentException> {
            C4Properties(
                values = listOf(
                    listOf("", "", "", "", "")
                )
            )
        }
    }

    @Test
    fun `should throw exception if num rows greater header rows`() {
        assertThrows<IllegalArgumentException> {
            C4Properties(
                header = listOf("", ""),
                values = listOf(
                    listOf("", ""),
                    listOf("", "", "")
                )
            )
        }
    }

    @Test
    fun `should not throw exception if num rows equal or smaller header rows`() {
        assertDoesNotThrow {
            C4Properties(
                header = listOf("", ""),
                values = listOf(
                    listOf("", ""),
                    listOf("")
                )
            )
        }
    }

    @Test
    fun `should not throw exception if num rows equal max rows`() {
        assertDoesNotThrow {
            C4Properties(
                values = listOf(
                    listOf("", "", "", "")
                )
            )
        }
    }

    @Test
    fun `should retain property order`() {
        val diagramKey = "C4PropertiesOrderTest"
        val values = listOf(
            listOf("b", "x", "y"),
            listOf("a", "x", "y"),
            listOf("c", "x", "y"),
            listOf("1", "x", "y")
        )
        val workspace = Workspace("My Workspace", "Some Description")
        val systemAsc = workspace.model.softwareSystem(
            "test asc",
            "test asc",
            properties = C4Properties(values = values.sortedBy { it.first() })
        )
        val systemDesc = workspace.model.softwareSystem(
            "test desc",
            "test desc",
            properties = C4Properties(values = values.sortedBy { it.first() }.reversed())
        )
        workspace.views.systemContextView(
            systemAsc,
            diagramKey,
            "properties should retain order"
        ).addAllSoftwareSystems()

        assertExpectedDiagramWasWrittenForView(workspace, diagramKey)
    }
}
