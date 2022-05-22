package com.github.chriskn.structurizrextension.model

import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.DeploymentElement
import com.structurizr.model.DeploymentNode
import com.structurizr.model.Location
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.model.StaticStructureElement

@Suppress("LongParameterList")
fun Model.person(
    name: String,
    description: String = "",
    icon: String? = null,
    location: Location = Location.Unspecified,
    link: String? = null,
    tags: List<String> = listOf(),
    properties: C4Properties? = null,
    uses: List<Dependency<StaticStructureElement>> = listOf(),
): Person {
    val person = this.addPerson(location, name, description)
    person.configure(icon, link, tags, properties)
    uses.forEach { dep ->
        when (val element = dep.target) {
            is SoftwareSystem -> person.uses(element, dep.description, dep.technology)
            is Container -> person.uses(element, dep.description, dep.technology)
            is Component -> person.uses(element, dep.description, dep.technology)
            is Person -> throw IllegalArgumentException("Person can't use Person")
        }
    }
    return person
}

@Suppress("LongParameterList")
fun Model.softwareSystem(
    name: String,
    description: String,
    location: Location = Location.Unspecified,
    type: C4Type? = null,
    icon: String? = null,
    link: String? = null,
    tags: List<String> = listOf(),
    properties: C4Properties? = null,
    uses: List<Dependency<StaticStructureElement>> = listOf(),
    usedBy: List<Dependency<StaticStructureElement>> = listOf()
): SoftwareSystem {
    val softwareSystem = this.addSoftwareSystem(location, name, description)
    softwareSystem.type = type
    softwareSystem.configure(icon, link, tags, properties)
    uses.forEach { dep ->
        when (dep.target) {
            is SoftwareSystem -> softwareSystem.uses(dep.target, dep.description, dep.technology, dep.interactionStyle)
            is Container -> softwareSystem.uses(dep.target, dep.description, dep.technology, dep.interactionStyle)
            is Component -> softwareSystem.uses(dep.target, dep.description, dep.technology, dep.interactionStyle)
            is Person -> throw IllegalArgumentException("SoftwareSystem can't use Person")
        }
    }
    usedBy.forEach { dep ->
        when (dep.target) {
            is SoftwareSystem -> dep.target.uses(softwareSystem, dep.description, dep.technology, dep.interactionStyle)
            is Container -> dep.target.uses(softwareSystem, dep.description, dep.technology, dep.interactionStyle)
            is Component -> dep.target.uses(softwareSystem, dep.description, dep.technology, dep.interactionStyle)
            is Person -> dep.target.uses(softwareSystem, dep.description, dep.technology)
        }
    }
    return softwareSystem
}

@Suppress("LongParameterList")
fun Model.deploymentNode(
    name: String,
    description: String = "",
    environment: String? = DeploymentElement.DEFAULT_DEPLOYMENT_ENVIRONMENT,
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
