package com.github.chriskn.structurizrextension

import com.structurizr.Workspace
import com.structurizr.io.plantuml.ExtendedC4PlantUmlWriter
import com.structurizr.io.plantuml.PlantUMLDiagram
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

/**
 * Writes all views of the workspace as C4PlantUML diagrams to the given output folder.
 *
 * Diagrams are named after there key
 *
 * @throws IOException if writing fails.
 */
fun Workspace.writeDiagrams(outputFolder: File) {
    outputFolder.mkdirs()
    val plantUMLWriter = ExtendedC4PlantUmlWriter()
    val diagrams = plantUMLWriter.toPlantUMLDiagrams(this)
    for (diagram in diagrams) {
        writeDiagram(diagram, File(outputFolder, "${diagram.key}.puml"))
    }
}

private fun writeDiagram(diagram: PlantUMLDiagram, out: File) {
    val writer = BufferedWriter(FileWriter(out))
    writer.write(diagram.definition)
    writer.close()
}
