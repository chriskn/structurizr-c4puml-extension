package com.github.chriskn.structurizrextension.api.view.sprite.sprites

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
 */
data class PlantUmlSprite(
    override val name: String,
    val path: String,
    val reference: String = defaultReference(path),
    val color: String? = null,
    val scale: Double? = null,
    val additionalIncludes: Set<String>? = null,
    val additionalDefinitions: Set<String>? = null,
) : Sprite(name, scale) {

    companion object {
        private const val PLANT_UML_FILE_EXTENSION = ".puml"
        private fun defaultReference(path: String): String = path
            .substringAfterLast("/")
            .replace(PLANT_UML_FILE_EXTENSION, "")
            .replace(">", "")
    }

    @get:JsonIgnore
    internal val colorValidated: String? = color?.let { toValidColor(color) }

    init {
        require(name.isNotBlank()) { "Icon name must not be blank" }
        validatePath(path)
        additionalIncludes?.forEach { validatePath(it) }
    }

    private fun validatePath(path: String) {
        val pathIsStandardLibReference = path.startsWith("<") && path.endsWith(">")
        val pathPointsToPumlFile = path.endsWith(PLANT_UML_FILE_EXTENSION)
        require(pathPointsToPumlFile || pathIsStandardLibReference) {
            "Icon URL needs to point to $PLANT_UML_FILE_EXTENSION file or must be a reference to the Plantuml StdLib but was $path"
        }
    }
}
