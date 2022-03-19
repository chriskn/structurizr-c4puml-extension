package com.github.chriskn.structurizrextension.model

import com.structurizr.io.plantuml.C4PlantUMLWriter
import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem

@Suppress("LongParameterList")
fun SoftwareSystem.addContainer(
    name: String,
    description: String,
    type: C4Type? = null,
    icon: String? = null,
    link: String? = null,
    technology: String = "",
    tags: List<String> = listOf(),
    properties: C4Properties? = null,
    uses: List<Dependency> = listOf(),
    usedBy: List<Dependency> = listOf()
): Container {
    val container = this.addContainer(name, description, technology)
    container.type = type
    container.configure(icon, link, tags, properties)
    uses.forEach { d ->
        when (d.element) {
            is SoftwareSystem -> container.uses(d.element, d.description, d.technology, d.interactionStyle)
            is Container -> container.uses(d.element, d.description, d.technology, d.interactionStyle)
            is Component -> container.uses(d.element, d.description, d.technology, d.interactionStyle)
        }
    }
    usedBy.forEach { d ->
        when (d.element) {
            is SoftwareSystem -> d.element.uses(container, d.description, d.technology, d.interactionStyle)
            is Container -> d.element.uses(container, d.description, d.technology, d.interactionStyle)
            is Component -> d.element.uses(container, d.description, d.technology, d.interactionStyle)
            is Person -> d.element.uses(container, d.description, d.technology)
        }
    }
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
