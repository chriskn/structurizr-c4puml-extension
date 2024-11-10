package com.github.chriskn.structurizrextension.api.model

import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.api.view.sprite.Sprite
import com.structurizr.model.Element
import com.structurizr.model.InteractionStyle

/**
 * Dependency used to model relationships between elements.
 *
 * @param destination       the destination of the dependency
 * @param description       the description of the dependency
 * @param technology        the technology of the dependency
 * @param interactionStyle  the [InteractionStyle] of the dependency
 * @param icon              the icon of the dependency. See [IconRegistry] for available icons or add your own
 * @param sprite            the sprite of the dependency. See [Sprite] implementations for sprite types
 * @param link              the link of the dependency
 * @param tags              the list of tags of the dependency
 * @param properties        [C4Properties] of the dependency
 * @return a new Dependency instance
 */
data class Dependency<out T : Element>(
    val destination: T,
    val description: String,
    val technology: String? = null,
    val interactionStyle: InteractionStyle? = null,
    @Deprecated("Use sprite API instead", replaceWith = ReplaceWith("sprite"))
    val icon: String? = null,
    val sprite: Sprite? = null,
    val link: String? = null,
    val tags: List<String> = listOf(),
    val properties: C4Properties? = null,
)
