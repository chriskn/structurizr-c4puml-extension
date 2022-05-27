package com.github.chriskn.structurizrextension.model

import com.structurizr.io.plantuml.C4PlantUMLWriter
import com.structurizr.model.Container
import com.structurizr.model.Location
import com.structurizr.model.SoftwareSystem
import com.structurizr.model.StaticStructureElement

@Suppress("LongParameterList")
fun SoftwareSystem.container(
    name: String,
    description: String,
    location: Location = this.location,
    type: C4Type? = null,
    icon: String? = null,
    link: String? = null,
    technology: String = "",
    tags: List<String> = listOf(),
    properties: C4Properties? = null,
    uses: List<Dependency<StaticStructureElement>> = listOf(),
    usedBy: List<Dependency<StaticStructureElement>> = listOf()
): Container {
    val container = this.addContainer(name, description, technology)
    container.type = type
    container.location = location
    container.configure(icon, link, tags, properties, uses, usedBy)
    return container
}

var SoftwareSystem.type: C4Type?
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
