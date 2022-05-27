package com.github.chriskn.structurizrextension.model

import com.structurizr.model.Element
import com.structurizr.model.InteractionStyle
import com.structurizr.model.StaticStructureElement

data class Dependency<out T : Element>(
    val target: T,
    val description: String,
    val technology: String? = null,
    val interactionStyle: InteractionStyle? = null,
    val link: String? = null,
    val properties: C4Properties? = null,
    val icon: String? = null,
    val tags: List<String> = listOf(),
)

fun Dependency<StaticStructureElement>.addRelationShipTo(target: StaticStructureElement) {
    this.target.uses(target, description, technology, interactionStyle)
        ?.configure(this.icon, this.link, this.tags, this.properties)
}

fun Dependency<StaticStructureElement>.addRelationShipFrom(source: StaticStructureElement) {
    source.uses(this.target, this.description, this.technology, this.interactionStyle)
        ?.configure(this.icon, this.link, this.tags, this.properties)
}
