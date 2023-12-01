package com.github.chriskn.structurizrextension

import com.structurizr.Workspace
import org.assertj.core.api.Assertions.assertThat
import java.io.File
import java.nio.file.Files.createTempDirectory

fun assertExpectedDiagramWasWrittenForView(
    workspace: Workspace,
    diagramKey: String
) {
    val expectedDiagram = object {}::class.java.getResource("/expected/$diagramKey.puml")
        ?: throw IllegalArgumentException("expected diagram file $diagramKey.puml not found")
    val expectedDiagramContent = expectedDiagram.readText(Charsets.UTF_8)
    val diagramFolder = createTempDirectory("diagram").toFile()
    workspace.writeDiagrams(diagramFolder)

    assertThat(diagramFolder.isDirectory).isTrue
    val actualDiagramFile = File(diagramFolder, "$diagramKey.puml")
    assertThat(actualDiagramFile.isFile).isTrue
    assertThat(actualDiagramFile.readText(Charsets.UTF_8)).isEqualToIgnoringWhitespace(
        expectedDiagramContent
    )
}
