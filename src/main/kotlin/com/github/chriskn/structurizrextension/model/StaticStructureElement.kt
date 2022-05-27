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
