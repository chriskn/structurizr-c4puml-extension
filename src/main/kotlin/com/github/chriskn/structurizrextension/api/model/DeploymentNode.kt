package com.github.chriskn.structurizrextension.api.model

import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.api.view.style.sprite.Sprite
import com.structurizr.model.Container
import com.structurizr.model.DeploymentElement
import com.structurizr.model.DeploymentNode
import com.structurizr.model.InfrastructureNode
import com.structurizr.model.SoftwareSystem

/**
 * Adds a child deployment node.
 *
 * @param name              the name of the deployment node
 * @param description       the description of the deployment node
 * @param icon              the icon of the deployment node. See [IconRegistry] for available icons or add your own
 * @param sprite            the sprite of the deployment node. See [Sprite] implementations for sprite types
 * @param link              the link of the deployment node
 * @param technology        the technology of the deployment node
 * @param tags              the list of tags of the deployment node
 * @param properties        the [C4Properties] of the deployment node
 * @param uses              the list of [Dependency] to a deployment node or infrastructure node the deployment node uses
 * @param usedBy            the list of [Dependency] to a deployment node or infrastructure node the deployment node is used by
 * @param hostsSystems      the list of systems the deployment node hosts
 * @param hostsContainers   the list of container the deployment node hosts
 * @return the resulting DeploymentNode
 */
@Suppress("LongParameterList")
fun DeploymentNode.deploymentNode(
    name: String,
    description: String = "",
    @Suppress("DEPRECATED_JAVA_ANNOTATION")
    @java.lang.Deprecated(since = "Since 12.2. Use sprite API instead")
    icon: String? = null,
    sprite: Sprite? = null,
    link: String? = null,
    technology: String = "",
    tags: List<String> = listOf(),
    properties: C4Properties? = null,
    uses: List<Dependency<DeploymentElement>> = listOf(),
    usedBy: List<Dependency<DeploymentElement>> = listOf(),
    hostsSystems: List<SoftwareSystem> = listOf(),
    hostsContainers: List<Container> = listOf(),
): DeploymentNode {
    val node = this.addDeploymentNode(name, description, technology)
    node.configure(icon, sprite, link, tags, properties, hostsSystems, hostsContainers, uses, usedBy)
    return node
}

/**
 * Adds a child infrastructure node.
 *
 * @param name              the name of the infrastructure node
 * @param description       the description of the infrastructure node
 * @param icon              the icon of the infrastructure node. See [IconRegistry] for available icons or add your own
 * @param sprite            the sprite of the infrastructure node. See [Sprite] implementations for sprite types
 * @param link              the link of the infrastructure node
 * @param technology        the technology of the infrastructure node
 * @param tags              the list of tags of the infrastructure node
 * @param properties        [C4Properties] of the infrastructure node
 * @param uses              the list of [Dependency] to a deployment node or infrastructure node the infrastructure node uses
 * @param usedBy            the list of [Dependency] to a deployment node or infrastructure node the infrastructure node is used by
 * @return the resulting DeploymentNode
 * @throws IllegalArgumentException if an invalid element is used in dependencies
 */
@Suppress("LongParameterList")
fun DeploymentNode.infrastructureNode(
    name: String,
    description: String = "",
    @Suppress("DEPRECATED_JAVA_ANNOTATION")
    @java.lang.Deprecated(since = "Since 12.2. Use sprite API instead")
    icon: String? = null,
    sprite: Sprite? = null,
    link: String? = null,
    technology: String = "",
    tags: List<String> = listOf(),
    properties: C4Properties? = null,
    uses: List<Dependency<DeploymentElement>> = listOf(),
    usedBy: List<Dependency<DeploymentNode>> = listOf(),
): InfrastructureNode {
    val node = this.addInfrastructureNode(name, description, technology)
    node.configure(icon, sprite, link, tags, properties)
    uses.forEach { dep ->
        node.uses(dep.destination, dep.description, dep.technology, dep.interactionStyle)
            .configure(dep.icon, dep.sprite, dep.link, dep.tags, dep.properties)
    }
    usedBy.forEach { dep ->
        dep.destination.uses(node, dep.description, dep.technology, dep.interactionStyle)
            .configure(dep.icon, dep.sprite, dep.link, dep.tags, dep.properties)
    }
    return node
}

@Suppress("LongParameterList")
internal fun DeploymentNode.configure(
    @Suppress("DEPRECATED_JAVA_ANNOTATION")
    @java.lang.Deprecated(since = "Since 12.2. Use sprite API instead")
    icon: String?,
    sprite: Sprite?,
    link: String?,
    tags: List<String>,
    properties: C4Properties?,
    hostsSystems: List<SoftwareSystem>,
    hostsContainers: List<Container>,
    uses: List<Dependency<DeploymentElement>>,
    usedBy: List<Dependency<DeploymentElement>>,
) {
    this.configure(icon, sprite, link, tags, properties)
    hostsSystems.forEach {
        val instance = this.add(it)
        it.tagsAsSet.forEach { tag -> instance.addTags(tag) }
    }
    hostsContainers.forEach {
        val instance = this.add(it)
        it.tagsAsSet.forEach { tag -> instance.addTags(tag) }
    }
    uses.forEach { dep ->
        when (dep.destination) {
            is DeploymentNode -> this.uses(dep.destination, dep.description, dep.technology, dep.interactionStyle)
                .configure(dep.icon, dep.sprite, dep.link, dep.tags, dep.properties)

            is InfrastructureNode -> this.uses(dep.destination, dep.description, dep.technology, dep.interactionStyle)
                .configure(dep.icon, dep.sprite, dep.link, dep.tags, dep.properties)

            else -> throw IllegalArgumentException("DeploymentNode cant use ${dep.destination::class.java.name}")
        }
    }
    usedBy.forEach { dep ->
        when (dep.destination) {
            is DeploymentNode -> dep.destination.uses(this, dep.description, dep.technology, dep.interactionStyle)
                .configure(dep.icon, dep.sprite, dep.link, dep.tags, dep.properties)

            is InfrastructureNode -> dep.destination.uses(this, dep.description, dep.technology, dep.interactionStyle)
                .configure(dep.icon, dep.sprite, dep.link, dep.tags, dep.properties)

            else -> throw IllegalArgumentException("DeploymentNode cant be use by ${dep.destination::class.java.name}")
        }
    }
}
