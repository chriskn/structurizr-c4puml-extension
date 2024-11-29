package com.github.chriskn.structurizrextension.api.model

import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.api.view.sprite.sprites.Sprite
import com.structurizr.model.InteractionStyle
import com.structurizr.model.Person
import com.structurizr.model.StaticStructureElement

/**
 * Adds a relationship to [destination] with the given properties
 *
 * @param destination       the destination of the dependency
 * @param description       the description of the dependency
 * @param technology        the technology of the dependency
 * @param interactionStyle  the [InteractionStyle] of the dependency
 * @param icon              the icon of the dependency. See [IconRegistry] for available icons or add your own
 * @param link              the link of the dependency
 * @param tags              the list of tags of the dependency
 * @param properties        [C4Properties] of the dependency
 */
@Suppress("LongParameterList")
fun <T : StaticStructureElement> StaticStructureElement.uses(
    destination: T,
    description: String,
    technology: String? = null,
    interactionStyle: InteractionStyle? = null,
    @Suppress("DEPRECATED_JAVA_ANNOTATION")
    @java.lang.Deprecated(since = "Since 12.2. Use sprite API instead")
    icon: String? = null,
    sprite: Sprite? = null,
    link: String? = null,
    tags: List<String> = listOf(),
    properties: C4Properties? = null,
) {
    Dependency(destination, description, technology, interactionStyle, icon, sprite, link, tags, properties)
        .addRelationshipFromNonPerson(this)
}

/**
 * Adds a relationship from [source] with the given properties
 *
 * @param source            the source of the dependency
 * @param description       the description of the dependency
 * @param technology        the technology of the dependency
 * @param interactionStyle  the [InteractionStyle] of the dependency
 * @param icon              the icon of the dependency. See [IconRegistry] for available icons or add your own
 * @param sprite            the sprite of the person. See [Sprite] implementations for sprite types
 * @param link              the link of the dependency
 * @param tags              the list of tags of the dependency
 * @param properties        [C4Properties] of the dependency
 */
@Suppress("LongParameterList")
fun <T : StaticStructureElement> StaticStructureElement.usedBy(
    source: T,
    description: String,
    technology: String? = null,
    interactionStyle: InteractionStyle? = null,
    @Suppress("DEPRECATED_JAVA_ANNOTATION")
    @java.lang.Deprecated(since = "Since 12.2. Use sprite API instead")
    icon: String? = null,
    sprite: Sprite? = null,
    link: String? = null,
    tags: List<String> = listOf(),
    properties: C4Properties? = null,
) {
    source.uses(this, description, technology, interactionStyle)
        ?.configure(icon, sprite, link, tags, properties)
}

internal fun StaticStructureElement.configure(
    icon: String?,
    sprite: Sprite?,
    link: String?,
    tags: List<String>,
    properties: C4Properties?,
    uses: List<Dependency<StaticStructureElement>>,
    usedBy: List<Dependency<StaticStructureElement>>
) {
    this.configure(icon, sprite, link, tags, properties)
    uses.forEach { dep -> dep.addRelationshipFromNonPerson(this) }
    usedBy.forEach { dep -> dep.addRelationShipTo(this) }
}

private fun Dependency<StaticStructureElement>.addRelationShipTo(target: StaticStructureElement) {
    this.destination.uses(target, description, technology, interactionStyle)
        ?.configure(this.icon, this.sprite, this.link, this.tags, this.properties)
}

internal fun Dependency<StaticStructureElement>.addRelationShipFrom(source: StaticStructureElement) {
    source.uses(this.destination, this.description, this.technology, this.interactionStyle)
        ?.configure(this.icon, this.sprite, this.link, this.tags, this.properties)
}

private fun Dependency<StaticStructureElement>.addRelationshipFromNonPerson(source: StaticStructureElement) {
    when (this.destination) {
        is Person -> throw IllegalArgumentException("${this::class.java.name} can't use Person")
        else -> this.addRelationShipFrom(source)
    }
}
