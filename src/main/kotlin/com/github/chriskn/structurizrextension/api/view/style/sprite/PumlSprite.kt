package com.github.chriskn.structurizrextension.api.view.style.sprite

import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import java.net.MalformedURLException

data class PumlSprite(
    /**
     * Url used for include statement
     *
     * @throws IllegalArgumentException if url does not point to puml file
     * @throws MalformedURLException if url is invalid
     */
    val includeUrl: String,
    val name: String,
    val color: String? = null,
    override val scale: Double? = null,
) : Sprite {

    init {
        // TODO is the registry still needed?
        IconRegistry.addIcon(name, includeUrl)
    }
}
