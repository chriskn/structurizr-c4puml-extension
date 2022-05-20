package com.github.chriskn.structurizrextension.model

import com.structurizr.io.plantuml.C4PlantUMLWriter
import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem

@Suppress("LongParameterList")
fun Container.component(
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
    uses.forEach { dep ->
        when (dep.target) {
            is SoftwareSystem -> component.uses(dep.target, dep.description, dep.technology, dep.interactionStyle)
            is Container -> component.uses(dep.target, dep.description, dep.technology, dep.interactionStyle)
            is Component -> component.uses(dep.target, dep.description, dep.technology, dep.interactionStyle)
        }
    }
    usedBy.forEach { dep ->
        when (dep.target) {
            is SoftwareSystem -> dep.target.uses(component, dep.description, dep.technology, dep.interactionStyle)
            is Container -> dep.target.uses(component, dep.description, dep.technology, dep.interactionStyle)
            is Component -> dep.target.uses(component, dep.description, dep.technology, dep.interactionStyle)
            is Person -> dep.target.uses(component, dep.description, dep.technology)
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
