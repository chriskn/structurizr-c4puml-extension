package com.github.chriskn.structurizrextension.model

import com.structurizr.Workspace
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class StaticStructureElementTest {

    @Test
    fun `throws IllegalArgumentException if element uses person`() {
        val model = Workspace("", "").model
        val person = model.person("test", "")

        assertThrows<IllegalArgumentException> {
            model.softwareSystem("test system", "", uses = listOf(Dependency(person, "invalid")))
        }
    }
}
