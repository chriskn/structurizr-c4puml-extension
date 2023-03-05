package com.github.chriskn.structurizrextension

import com.github.chriskn.structurizrextension.export.ExtendedC4PlantUMLExporter
import com.structurizr.Workspace
import com.structurizr.export.Diagram
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

/**
 * Writes all views of the workspace as C4PlantUML diagrams to the given output folder.
 *
 * Diagrams files are named after their diagram key
 *
 * @throws IOException if writing fails.
 */
fun Workspace.writeDiagrams(outputFolder: File) {
    outputFolder.mkdirs()
    val diagrams = ExtendedC4PlantUMLExporter().export(this)
    for (diagram in diagrams) {
        writeDiagram(diagram, File(outputFolder, "${diagram.key}.puml"))
    }
}

private fun writeDiagram(diagram: Diagram, out: File) {
    val writer = BufferedWriter(FileWriter(out))
    writer.write(diagram.definition)
    writer.close()
}
