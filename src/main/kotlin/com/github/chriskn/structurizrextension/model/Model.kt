package com.github.chriskn.structurizrextension.model

import com.structurizr.model.Container
import com.structurizr.model.DeploymentElement
import com.structurizr.model.DeploymentNode
import com.structurizr.model.Location
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.model.StaticStructureElement

/**
 * Adds a person.
 *
 * @param name        the name of the person (e.g. "Admin User" or "Bob the Business User")
 * @param description the description of the person
 * @param location    the [Location]] of the person
 * @param icon        the icon of the person. See IconRegistry for available icons or add your own
 * @param link        the link of the person
 * @param tags        the list of tags of the person
 * @param properties  the [C4Properties] of the person
 * @param uses        the list of [Dependency] to a system, container or component the person uses.
 * @return the Person created and added to the model (or null)
 * @throws IllegalArgumentException if a person with the same name already exists
 */
@Suppress("LongParameterList")
fun Model.person(
    name: String,
    description: String = "",
    location: Location = Location.Unspecified,
    icon: String? = null,
    link: String? = null,
    tags: List<String> = listOf(),
    properties: C4Properties? = null,
    uses: List<Dependency<StaticStructureElement>> = listOf(),
): Person {
    @Suppress("DEPRECATION")
    val person = this.addPerson(name, description)
    person.c4Location = location
    person.configure(icon, link, tags, properties)
    uses.forEach { dep -> dep.addRelationShipFrom(person) }
    return person
}

/**
 * Adds a system.
 *
 * @param name          the name of the system
 * @param description   the description of the system
 * @param location      the [Location]] of the system
 * @param c4Type        the [C4Type] of the system
 * @param icon          the icon of the system. See IconRegistry for available icons or add your own
 * @param link          the link of the system
 * @param tags          the list of tags of the system
 * @param properties    the [C4Properties] of the system
 * @param uses          the list of [Dependency] to a system, container or component the system uses. A person can't be used
 * @param usedBy        the list of [Dependency] to a system, container, component or person the system is used by
 * @return the SoftwareSystem created and added to the model (or null)
 * @throws IllegalArgumentException if a software system with the same name already exists or a person is used in an uses dependency
 */
@Suppress("LongParameterList")
fun Model.softwareSystem(
    name: String,
    description: String,
    location: Location = Location.Unspecified,
    c4Type: C4Type? = null,
    icon: String? = null,
    link: String? = null,
    tags: List<String> = listOf(),
    properties: C4Properties? = null,
    uses: List<Dependency<StaticStructureElement>> = listOf(),
    usedBy: List<Dependency<StaticStructureElement>> = listOf()
): SoftwareSystem {
    val softwareSystem = this.addSoftwareSystem(name, description)
    softwareSystem.c4Type = c4Type
    softwareSystem.c4Location = location
    softwareSystem.configure(icon, link, tags, properties, uses, usedBy)
    return softwareSystem
}

/**
 * Adds a deployment node.
 *
 * @param name              the name of the deployment node
 * @param description       the description of the deployment node
 * @param icon              the icon of the deployment node. See IconRegistry for available icons or add your own
 * @param link              the link of the deployment node
 * @param technology        the technology of the deployment node
 * @param tags              the list of tags of the deployment node
 * @param properties        [C4Properties] of the deployment node
 * @param uses              the list of [Dependency] to a deployment node or infrastructure node the deployment node uses
 * @param usedBy            the list of [Dependency] to a deployment node or infrastructure node the deployment node is used by
 * @param hostsSystems      the list of systems the deployment node hosts
 * @param hostsContainers   the list of container the deployment node hosts
 * @return the resulting DeploymentNode
 * @throws IllegalArgumentException if an invalid element is used in dependencies
 */
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
