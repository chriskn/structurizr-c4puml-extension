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
    hostsSystems: List<SoftwareSystem> = listOf(),
    hostsContainers: List<Container> = listOf()
): DeploymentNode {
    val node = this.addDeploymentNode(name, description, technology)
    node.configure(icon, link, tags, properties)
    hostsSystems.forEach { node.add(it) }
    hostsContainers.forEach { node.add(it) }
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
    uses.forEach { dep -> node.uses(dep.target, dep.description, dep.technology, dep.interactionStyle) }
    usedBy.forEach { dep -> dep.target.uses(node, dep.description, dep.technology, dep.interactionStyle) }
    return node
}
