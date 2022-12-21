package com.github.chriskn.structurizrextension

import com.structurizr.Workspace
import org.assertj.core.api.Assertions
import java.io.File
import java.nio.file.Files.createTempDirectory

fun assertExpectedDiagramWasWrittenForView(
    workspace: Workspace,
    diagramKey: String
) {
    val expectedDiagramContent =
        object{}::class.java.getResource("/expected/$diagramKey.puml")!!.readText(Charsets.UTF_8)
    val diagramFolder = createTempDirectory("diagram").toFile()
    workspace.writeDiagrams(diagramFolder)

    Assertions.assertThat(diagramFolder.isDirectory).isTrue
    val actualDiagramFile = File(diagramFolder, "$diagramKey.puml")
    Assertions.assertThat(actualDiagramFile.isFile).isTrue
    Assertions.assertThat(actualDiagramFile.readText(Charsets.UTF_8)).isEqualToIgnoringWhitespace(
        expectedDiagramContent
    )
}
