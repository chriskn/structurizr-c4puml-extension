package com.github.chriskn.structurizrextension.model

import com.structurizr.io.plantuml.C4PlantUMLWriter
import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.Location
import com.structurizr.model.StaticStructureElement

const val LOCATION_PROPERTY = "c4location"

@Suppress("LongParameterList")
fun Container.component(
    name: String,
    description: String,
    icon: String? = null,
    link: String? = null,
    technology: String = "",
    tags: List<String> = listOf(),
    properties: C4Properties? = null,
    uses: List<Dependency<StaticStructureElement>> = listOf(),
    usedBy: List<Dependency<StaticStructureElement>> = listOf()
): Component {
    val component = this.addComponent(name, description, technology)
    component.configure(icon, link, tags, properties, uses, usedBy)
    return component
}

var Container.type: C4Type?
    get() = if (this.properties.containsKey(C4PlantUMLWriter.C4_ELEMENT_TYPE)) {
        C4Type.fromC4Type(this.properties.getValue(C4PlantUMLWriter.C4_ELEMENT_TYPE))
    } else {
        null
    }
    set(value) {
        if (value != null) {
            this.addProperty(C4PlantUMLWriter.C4_ELEMENT_TYPE, value.c4Type)
        }
    }

var Container.location: Location
    get() = Location.valueOf(this.properties.getValue(LOCATION_PROPERTY))
    set(location) {
        this.addProperty(LOCATION_PROPERTY, location.name)
    }
