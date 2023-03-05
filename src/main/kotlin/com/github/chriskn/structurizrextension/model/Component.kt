package com.github.chriskn.structurizrextension.model

import com.structurizr.export.plantuml.C4PlantUMLExporter
import com.structurizr.model.Component

var Component.c4Type: C4Type?
    /**
     * Returns the [C4Type] of the component.
     */
    get() = if (this.properties.containsKey(C4PlantUMLExporter.C4PLANTUML_SPRITE)) {
        C4Type.fromC4Type(this.properties.getValue(C4PlantUMLExporter.C4PLANTUML_SPRITE))
    } else {
        null
    }

    /**
     * Sets the [C4Type] of the component.
     */
    set(value) {
        if (value != null) {
            this.addProperty(C4PlantUMLExporter.C4PLANTUML_SPRITE, value.c4Type)
        }
    }
