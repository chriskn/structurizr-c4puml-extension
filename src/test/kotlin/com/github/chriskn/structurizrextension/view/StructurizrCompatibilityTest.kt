package com.github.chriskn.structurizrextension.view

import com.github.chriskn.structurizrextension.api.writeDiagrams
import com.structurizr.Workspace
import com.structurizr.model.Model
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

/**
 * Tests the combination of models created directly from Structurizr and models created with the extension. This
 * is especially required as the Location has been removed from the Structurizr API and is now part of the extension.
 *
 * @see https://github.com/chriskn/structurizr-c4puml-extension/issues/236
 */
class StructurizrCompatibilityTest {

    private lateinit var workspace: Workspace
    private lateinit var model: Model

    @BeforeEach
    fun setUp() {
        workspace = Workspace("", "")
        model = workspace.model
    }

    @Test
    fun `addPerson can be called directly on the Structurizr Methods`() {
        model.addPerson("Person 1", "Description")
        workspace.views.createSystemLandscapeView("SystemLandscape", "Description").addAllElements()
        workspace.writeDiagrams(File("build/structurizr"))
    }

    @Test
    fun `addSoftwareSystem can be called directly on the Structurizr Methods`() {
        model.addSoftwareSystem("Software System")
        workspace.views.createSystemLandscapeView("SystemLandscape", "Description").addAllElements()
        workspace.writeDiagrams(File("build/structurizr"))
    }

    @Test
    fun `addContainer can be called directly on the Structurizr Methods`() {
        val softwareSystem = model.addSoftwareSystem("Software System")
        softwareSystem.addContainer("Container")
        workspace.views.createContainerView(softwareSystem, "Container", "Container").addAllElements()
        workspace.writeDiagrams(File("build/structurizr"))
    }
}
