package com.github.chriskn.structurizrextension.model

import com.github.chriskn.structurizrextension.api.model.C4Properties
import com.github.chriskn.structurizrextension.api.model.softwareSystem
import com.github.chriskn.structurizrextension.api.model.uses
import com.github.chriskn.structurizrextension.api.view.systemContextView
import com.github.chriskn.structurizrextension.assertExpectedDiagramWasWrittenForView
import com.structurizr.Workspace
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class C4PropertiesTest {

    @Test
    fun `throws exception if num columns greater max columns`() {
        assertThrows<IllegalArgumentException> {
            C4Properties(
                values = listOf(
                    listOf("", "", "", "", "")
                )
            )
        }
    }

    @Test
    fun `should throw exception if num columns greater header columns`() {
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
    fun `should throw exception if header num columns smaller values num columns`() {
        assertThrows<IllegalArgumentException> {
            C4Properties(
                header = listOf("1"),
                values = listOf(
                    listOf("1", "2"),
                )
            )
        }
    }

    @Test
    fun `should not throw exception if num columns equal or smaller header columns`() {
        assertDoesNotThrow {
            C4Properties(
                header = listOf("", "", ""),
                values = listOf(
                    listOf("", "")
                )
            )
        }
    }

    @Test
    fun `should not throw exception if num columns equal max columns`() {
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
        val workspace = Workspace("My Workspace", "Some Description")
        val values = listOf(
            listOf("b", "x", "y"),
            listOf("a", "x", "y"),
            listOf("c", "x", "y"),
            listOf("1", "x", "y")
        )
        val propertiesAsc = C4Properties(values = values.sortedBy { it.first() })
        val propertiesDesc = C4Properties(values = values.sortedBy { it.first() }.reversed())
        val systemAsc = workspace.model.softwareSystem(
            "test asc",
            "test asc",
            properties = propertiesAsc
        )
        val systemDesc = workspace.model.softwareSystem(
            "test desc",
            "test desc",
            properties = propertiesDesc
        )
        systemAsc.uses(systemDesc, "Asc", properties = propertiesAsc)
        systemDesc.uses(systemAsc, "Desc", properties = propertiesDesc)

        workspace.views.systemContextView(
            systemAsc,
            diagramKey,
            "properties should retain order"
        ).addAllSoftwareSystems()

        assertExpectedDiagramWasWrittenForView(workspace, "model", diagramKey)
    }
}
