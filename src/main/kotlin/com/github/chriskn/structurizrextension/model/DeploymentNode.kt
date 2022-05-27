package com.github.chriskn.structurizrextension.model

import com.structurizr.model.Container
import com.structurizr.model.DeploymentElement
import com.structurizr.model.DeploymentNode
import com.structurizr.model.InfrastructureNode
import com.structurizr.model.SoftwareSystem

@Suppress("LongParameterList")
fun DeploymentNode.deploymentNode(
    name: String,
    description: String = "",
    icon: String? = null,
    link: String? = null,
    technology: String = "",
    tags: List<String> = listOf(),
    properties: C4Properties = C4Properties(values = listOf()),
    uses: List<Dependency<DeploymentElement>> = listOf(),
    usedBy: List<Dependency<DeploymentElement>> = listOf(),
    hostsSystems: List<SoftwareSystem> = listOf(),
    hostsContainers: List<Container> = listOf()
): DeploymentNode {
    val node = this.addDeploymentNode(name, description, technology)
    node.configure(icon, link, tags, properties, hostsSystems, hostsContainers, uses, usedBy)
    return node
}

@Suppress("LongParameterList")
fun DeploymentNode.infrastructureNode(
    name: String,
    description: String = "",
    icon: String? = null,
    link: String? = null,
    technology: String = "",
    tags: List<String> = listOf(),
    properties: C4Properties = C4Properties(values = listOf()),
    uses: List<Dependency<DeploymentElement>> = listOf(),
    usedBy: List<Dependency<DeploymentNode>> = listOf()
): InfrastructureNode {
    val node = this.addInfrastructureNode(name, description, technology)
    node.configure(icon, link, tags, properties)
    uses.forEach { dep ->
        node.uses(dep.destination, dep.description, dep.technology, dep.interactionStyle)
            ?.configure(dep.icon, dep.link, dep.tags, dep.properties)
    }
    usedBy.forEach { dep ->
        dep.destination.uses(node, dep.description, dep.technology, dep.interactionStyle)
            ?.configure(dep.icon, dep.link, dep.tags, dep.properties)
    }
    return node
}

@Suppress("LongParameterList")
fun DeploymentNode.configure(
    icon: String?,
    link: String?,
    tags: List<String>,
    properties: C4Properties,
    hostsSystems: List<SoftwareSystem>,
    hostsContainers: List<Container>,
    uses: List<Dependency<DeploymentElement>>,
    usedBy: List<Dependency<DeploymentElement>>,
) {
    this.configure(icon, link, tags, properties)
    hostsSystems.forEach { this.add(it) }
    hostsContainers.forEach { this.add(it) }
    uses.forEach { dep ->
        when (dep.destination) {
            is DeploymentNode -> this.uses(dep.destination, dep.description, dep.technology, dep.interactionStyle)
                ?.configure(dep.icon, dep.link, dep.tags, dep.properties)
            is InfrastructureNode -> this.uses(dep.destination, dep.description, dep.technology, dep.interactionStyle)
                ?.configure(dep.icon, dep.link, dep.tags, dep.properties)
            else -> throw IllegalArgumentException("DeploymentNode cant use ${dep.destination::class.java.name}")
        }
    }
    usedBy.forEach { dep ->
        when (dep.destination) {
            is DeploymentNode -> dep.destination.uses(this, dep.description, dep.technology, dep.interactionStyle)
                ?.configure(dep.icon, dep.link, dep.tags, dep.properties)
            is InfrastructureNode -> dep.destination.uses(this, dep.description, dep.technology, dep.interactionStyle)
                ?.configure(dep.icon, dep.link, dep.tags, dep.properties)
            else -> throw IllegalArgumentException("DeploymentNode cant be use by ${dep.destination::class.java.name}")
        }
    }
}
