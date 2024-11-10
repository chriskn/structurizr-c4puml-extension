package com.github.chriskn.structurizrextension.api.model

import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.api.view.sprite.Sprite
import com.structurizr.model.Container
import com.structurizr.model.Location
import com.structurizr.model.SoftwareSystem
import com.structurizr.model.StaticStructureElement

/**
 * Adds a container.
 *
 * @param name          the name of the container
 * @param description   the description of the container
 * @param location      the [Location] of the container
 * @param c4Type        the [C4Type] of the container
 * @param icon          the icon of the container. See [IconRegistry] for available icons or add your own
 * @param sprite        the sprite of the container. See [Sprite] implementations for sprite types
 * @param link          the link of the container
 * @param tags          the list of tags of the container
 * @param properties    the [C4Properties] of the container
 * @param uses          the list of [Dependency] to a system, container or component the container uses. A person can't be used
 * @param usedBy        the list of [Dependency] to a system, container, component or person the container is used by
 * @return the Container created and added to the system (or null)
 * @throws IllegalArgumentException if a container with the same name already exists or a person is used in an uses dependency
 */
@Suppress("LongParameterList")
fun SoftwareSystem.container(
    name: String,
    description: String,
    location: Location = this.c4Location,
    c4Type: C4Type? = null,
    @Suppress("DEPRECATED_JAVA_ANNOTATION")
    @java.lang.Deprecated(since = "Since 12.2. Use sprite API instead")
    icon: String? = null,
    sprite: Sprite? = null,
    link: String? = null,
    technology: String = "",
    tags: List<String> = listOf(),
    properties: C4Properties? = null,
    uses: List<Dependency<StaticStructureElement>> = listOf(),
    usedBy: List<Dependency<StaticStructureElement>> = listOf()
): Container {
    val container = this.addContainer(name, description, technology)
    container.c4Type = c4Type
    container.c4Location = location
    container.configure(icon, sprite, link, tags, properties, uses, usedBy)
    return container
}
