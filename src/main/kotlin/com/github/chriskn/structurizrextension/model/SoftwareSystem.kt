package com.github.chriskn.structurizrextension.model

import com.structurizr.io.plantuml.C4PlantUMLWriter
import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.model.StaticStructureElement

@Suppress("LongParameterList")
fun SoftwareSystem.container(
    name: String,
    description: String,
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
    container.configure(icon, link, tags, properties)
    uses.forEach { dep ->
        when (dep.target) {
            is SoftwareSystem -> container.uses(dep.target, dep.description, dep.technology, dep.interactionStyle)
            is Container -> container.uses(dep.target, dep.description, dep.technology, dep.interactionStyle)
            is Component -> container.uses(dep.target, dep.description, dep.technology, dep.interactionStyle)
            is Person -> throw IllegalArgumentException("SoftwareSystem can't use Person")
        }
    }
    usedBy.forEach { dep ->
        when (dep.target) {
            is SoftwareSystem -> dep.target.uses(container, dep.description, dep.technology, dep.interactionStyle)
            is Container -> dep.target.uses(container, dep.description, dep.technology, dep.interactionStyle)
            is Component -> dep.target.uses(container, dep.description, dep.technology, dep.interactionStyle)
            is Person -> dep.target.uses(container, dep.description, dep.technology)
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
