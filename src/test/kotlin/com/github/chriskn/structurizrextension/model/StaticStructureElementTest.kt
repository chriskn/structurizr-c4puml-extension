package com.github.chriskn.structurizrextension.model

import com.structurizr.Workspace
import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class StaticStructureElementTest {

    private lateinit var model: Model
    private lateinit var person: Person
    private lateinit var system: SoftwareSystem
    private lateinit var container: Container

    @BeforeAll
    fun setUp(){
        model = Workspace("", "").model
        person = model.person("test", "")
        system =  model.softwareSystem("test system", "")
        container = system.container("test container", "")
    }

    @Test
    fun `throws IllegalArgumentException if SoftwareSystem uses person`() {
        assertThrows<IllegalArgumentException> {
            model.softwareSystem("test system", "")
            system.uses(person, "invalid")
        }
    }


    @Test
    fun `throws IllegalArgumentException if Container uses person`() {
        assertThrows<IllegalArgumentException> {
            container.uses(person, "invalid")
        }
    }

    @Test
    fun `throws IllegalArgumentException if Component uses person`() {
            assertThrows<IllegalArgumentException> {
            container.component("component", "", uses = listOf(Dependency(person, "invalid")))
        }
    }
}
