package com.github.chriskn.structurizrextension.model

import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.DeploymentNode
import com.structurizr.model.Location
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem

@Suppress("LongParameterList")
fun Model.addPerson(
    name: String,
    description: String = "",
    icon: String? = null,
    location: Location = Location.Unspecified,
    link: String? = null,
    tags: List<String> = listOf(),
    properties: C4Properties? = null,
    uses: List<Dependency> = listOf(),
): Person {
    val person = this.addPerson(location, name, description)
    person.configure(icon, link, tags, properties)
    uses.forEach { d ->
        when (val element = d.element) {
            is SoftwareSystem -> person.uses(element, d.description, d.technology)
            is Container -> person.uses(element, d.description, d.technology)
            is Component -> person.uses(element, d.description, d.technology)
        }
    }
    return person
}

@Suppress("LongParameterList")
fun Model.addSoftwareSystem(
    name: String,
    description: String = "",
    location: Location = Location.Unspecified,
    type: C4Type? = null,
    icon: String? = null,
    link: String? = null,
    tags: List<String> = listOf(),
    properties: C4Properties? = null,
    uses: List<Dependency> = listOf(),
    usedBy: List<Dependency> = listOf()
): SoftwareSystem {
    val softwareSystem = this.addSoftwareSystem(location, name, description)
    softwareSystem.type = type
    softwareSystem.configure(icon, link, tags, properties)
    uses.forEach { d ->
        when (d.element) {
            is SoftwareSystem -> softwareSystem.uses(d.element, d.description, d.technology, d.interactionStyle)
            is Container -> softwareSystem.uses(d.element, d.description, d.technology, d.interactionStyle)
            is Component -> softwareSystem.uses(d.element, d.description, d.technology, d.interactionStyle)
        }
    }
    usedBy.forEach { d ->
        when (d.element) {
            is SoftwareSystem -> d.element.uses(softwareSystem, d.description, d.technology, d.interactionStyle)
            is Container -> d.element.uses(softwareSystem, d.description, d.technology, d.interactionStyle)
            is Component -> d.element.uses(softwareSystem, d.description, d.technology, d.interactionStyle)
            is Person -> d.element.uses(softwareSystem, d.description, d.technology)
        }
    }
    return softwareSystem
}

@Suppress("LongParameterList")
fun Model.addDeploymentNode(
    environment: String,
    name: String,
    description: String = "",
    icon: String? = null,
    link: String? = null,
    technology: String = "",
    tags: List<String> = listOf(),
    properties: C4Properties = C4Properties(values = listOf()),
    hostsSystems: List<SoftwareSystem> = listOf(),
    hostsContainers: List<Container> = listOf(),

): DeploymentNode {
    val node = this.addDeploymentNode(environment, name, description, technology)
    node.configure(icon, link, tags, properties)
    hostsSystems.forEach { node.add(it) }
    hostsContainers.forEach { node.add(it) }
    return node
}
