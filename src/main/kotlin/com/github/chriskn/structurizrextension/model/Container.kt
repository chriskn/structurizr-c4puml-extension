package com.github.chriskn.structurizrextension.model

import com.structurizr.io.plantuml.C4PlantUMLWriter
import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem



@Suppress("LongParameterList")
fun Container.addComponent(
    name: String,
    description: String,
    icon: String? = null,
    link: String? = null,
    technology: String = "",
    tags: List<String> = listOf(),
    properties: C4Properties? = null,
    uses: List<Dependency> = listOf(),
    usedBy: List<Dependency> = listOf()
): Component {
    val component = this.addComponent(name, description, technology)
    component.configure(icon, link, tags, properties)
    uses.forEach { d ->
        when (d.element) {
            is SoftwareSystem -> component.uses(d.element, d.description, d.technology, d.interactionStyle)
            is Container -> component.uses(d.element, d.description, d.technology, d.interactionStyle)
            is Component -> component.uses(d.element, d.description, d.technology, d.interactionStyle)
        }
    }
    usedBy.forEach { d ->
        when (d.element) {
            is SoftwareSystem -> d.element.uses(component, d.description, d.technology, d.interactionStyle)
            is Container -> d.element.uses(component, d.description, d.technology, d.interactionStyle)
            is Component -> d.element.uses(component, d.description, d.technology, d.interactionStyle)
            is Person -> d.element.uses(component, d.description, d.technology)
        }
    }
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
