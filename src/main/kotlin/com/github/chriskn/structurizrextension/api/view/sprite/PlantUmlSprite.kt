package com.github.chriskn.structurizrextension.api.view.sprite

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.chriskn.structurizrextension.internal.view.style.toValidColor

/**
 * Plant UML sprite
 *
 * Can be used to include plantuml icons.
 *
 * @property name                   the name of the icon
 * @property path                   url pointing to a .puml file or a path in the plantuml-stdlib.
 * @property color                  the color of the icon. Must be a valid hex code or a named color (e.g. "green")
 * @property scale                  the scale of the icon
 * @property additionalIncludes     list with urls that should be included additionally before the sprite is included
 * @property additionalDefinitions  each will be written as !define <additionalDefinition> in the output file.
 *                                  Can be used to define named urls. The names then can be used in the url
 *                                  Will be written before additionalIncludes
 * @constructor Create Plant UML sprite
 * @throws IllegalArgumentException if url or additionalIncludes do not point to a puml file or is a stdlib reference
 *
 */
data class PlantUmlSprite(
    override val name: String,
    val path: String,
    val reference: String = defaultReference(path),
    val color: String? = null,
    val scale: Double? = null,
    val additionalIncludes: List<String>? = null,
    val additionalDefinitions: List<String>? = null,
) : Sprite(name, scale) {

    companion object {
        private const val PUML_FILE_EXTENSION = ".puml"
        private fun defaultReference(path: String): String = path
            .substringAfterLast("/")
            .replace(PUML_FILE_EXTENSION, "")
            .replace(">", "")
    }

    @get:JsonIgnore
    internal val colorValidated: String? = color?.let { toValidColor(color) }

    init {
        require(name.isNotBlank()) { "Icon name must not be blank" }
        validateUrl(path)
        additionalIncludes?.forEach { validateUrl(it) }
    }

    private fun validateUrl(url: String) {
        require(url.endsWith(PUML_FILE_EXTENSION) || (url.startsWith("<") && url.endsWith(">"))) {
            "Icon URL needs to point to $PUML_FILE_EXTENSION file or must be a part of the Plantuml StdLib but was $url"
        }
    }
}
