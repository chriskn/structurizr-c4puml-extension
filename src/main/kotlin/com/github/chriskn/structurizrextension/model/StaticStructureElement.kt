package com.github.chriskn.structurizrextension.model

import com.structurizr.model.Person
import com.structurizr.model.StaticStructureElement

fun StaticStructureElement.configure(
    icon: String?,
    link: String?,
    tags: List<String>,
    properties: C4Properties?,
    uses: List<Dependency<StaticStructureElement>>,
    usedBy: List<Dependency<StaticStructureElement>>
) {
    this.configure(icon, link, tags, properties)
    uses.forEach { dep ->
        when (dep.destination) {
            is Person -> throw IllegalArgumentException("${this::class.java.name} can't use Person")
            else -> dep.addRelationShipFrom(this)
        }
    }
    usedBy.forEach { dep -> dep.addRelationShipTo(this) }
}

private fun Dependency<StaticStructureElement>.addRelationShipTo(target: StaticStructureElement) {
    this.destination.uses(target, description, technology, interactionStyle)
        ?.configure(this.icon, this.link, this.tags, this.properties)
}

fun Dependency<StaticStructureElement>.addRelationShipFrom(source: StaticStructureElement) {
    source.uses(this.destination, this.description, this.technology, this.interactionStyle)
        ?.configure(this.icon, this.link, this.tags, this.properties)
}
