package com.github.chriskn.structurizrextension.plantuml

import com.github.chriskn.structurizrextension.model.icon
import com.structurizr.Workspace
import com.structurizr.io.plantuml.ExtendedC4PlantUmlWriter
import com.structurizr.io.plantuml.PlantUMLDiagram
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

object C4PlantUmlDiagramWriter {

    fun writeDiagrams(outputFolder: File, workspace: Workspace) {
        outputFolder.mkdirs()
        val iconIncludes: MutableList<String> = workspace.model.elements
            .asSequence()
            .mapNotNull { it.icon?.let { technology -> IconRegistry.iconUrlFor(technology) } }
            .toSet()
            .toList()
            .sorted()
            .toMutableList()
        if (iconIncludes.any { it.startsWith(AWS_ICON_URL) }) {
            iconIncludes.add(0, AWS_ICON_COMMONS)
        }
        val plantUMLWriter = ExtendedC4PlantUmlWriter(iconIncludes)
        val diagrams = plantUMLWriter.toPlantUMLDiagrams(workspace)
        for (diagram in diagrams) {
            writeDiagram(diagram, File(outputFolder, "${diagram.key}.puml"))
        }
    }

    private fun writeDiagram(diagram: PlantUMLDiagram, out: File) {
        val writer = BufferedWriter(FileWriter(out))
        writer.write(diagram.definition)
        writer.close()
    }
}
