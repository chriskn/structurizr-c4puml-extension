package com.github.chriskn.structurizrextension.model

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
    uses.forEach { dep -> dep.addRelationShipFrom(person) }
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
    softwareSystem.configure(icon, link, tags, properties, uses, usedBy)
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
    uses: List<Dependency<DeploymentElement>> = listOf(),
    usedBy: List<Dependency<DeploymentElement>> = listOf(),
    hostsSystems: List<SoftwareSystem> = listOf(),
    hostsContainers: List<Container> = listOf(),
): DeploymentNode {
    val node = this.addDeploymentNode(environment, name, description, technology)
    node.configure(icon, link, tags, properties, hostsSystems, hostsContainers, uses, usedBy)
    return node
}
