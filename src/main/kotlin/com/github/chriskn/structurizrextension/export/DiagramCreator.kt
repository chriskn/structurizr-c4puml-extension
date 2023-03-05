package com.github.chriskn.structurizrextension.export

import com.structurizr.export.Diagram
import com.structurizr.export.plantuml.PlantUMLDiagram
import com.structurizr.view.ModelView

fun createC4Diagram(view: ModelView, definition: String): Diagram {
    return PlantUMLDiagram(view, definition)
}
