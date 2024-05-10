package com.github.chriskn.structurizrextension.model

import com.structurizr.export.plantuml.C4PlantUMLExporter
import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.SoftwareSystem

/**
 * Determines the shape of an Element in the rendered diagrams.
 */
enum class C4Type(val c4Type: String) {
    /**
     * The QUEUE type can be used to visualize e.g. message queues
     */
    QUEUE("Queue"),

    /**
     * The DATABASE type can be used to visualize databases
     */
    DATABASE("Db");

    companion object {
        fun fromC4Type(c4Type: String): C4Type {
            return entries.first { it.c4Type == c4Type }
        }
    }
}

var SoftwareSystem.c4Type: C4Type?
    /**
     * Returns the [C4Type] of the SoftwareSystem.
     */
    get() = if (this.properties.containsKey(C4PlantUMLExporter.C4PLANTUML_SPRITE)) {
        C4Type.fromC4Type(this.properties.getValue(C4PlantUMLExporter.C4PLANTUML_SPRITE))
    } else {
        null
    }

    /**
     * Sets the [C4Type] of the SoftwareSystem.
     */
    set(value) {
        if (value != null) {
            this.addProperty(C4PlantUMLExporter.C4PLANTUML_SPRITE, value.c4Type)
        }
    }

var Container.c4Type: C4Type?
    /**
     * Returns the [C4Type] of the container.
     */
    get() = if (this.properties.containsKey(C4PlantUMLExporter.C4PLANTUML_SPRITE)) {
        C4Type.fromC4Type(this.properties.getValue(C4PlantUMLExporter.C4PLANTUML_SPRITE))
    } else {
        null
    }

    /**
     * Sets the [C4Type] of the container.
     */
    set(value) {
        if (value != null) {
            this.addProperty(C4PlantUMLExporter.C4PLANTUML_SPRITE, value.c4Type)
        }
    }

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
