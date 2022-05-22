package com.github.chriskn.structurizrextension.plantuml

import com.structurizr.Workspace
import com.structurizr.io.plantuml.ExtendedC4PlantUmlWriter
import com.structurizr.io.plantuml.PlantUMLDiagram
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

object C4PlantUmlDiagramWriter {

    fun writeDiagrams(outputFolder: File, workspace: Workspace) {
        outputFolder.mkdirs()
        val plantUMLWriter = ExtendedC4PlantUmlWriter()
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
