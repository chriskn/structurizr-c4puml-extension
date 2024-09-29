package com.github.chriskn.structurizrextension.api.view.style.sprite

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.api.view.style.toValidColor
import java.net.MalformedURLException

data class PumlSprite(
    val name: String,
    /**
     * Url used for include statement
     *
     * @throws IllegalArgumentException if url does not point to puml file
     * @throws MalformedURLException if url is invalid
     */
    val includeUrl: String,
    val color: String? = null,
    val scale: Double? = null,
) : Sprite(scale) {

    @get:JsonIgnore
    internal val validatedColor: String? = color?.let { toValidColor(color) }

    init {
        require(name.isNotBlank()) { "name cannot be blank" }
        // TODO is the registry still needed?
        IconRegistry.addIcon(name, includeUrl)
    }
}
