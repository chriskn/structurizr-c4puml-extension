package com.github.chriskn.structurizrextension.api.model

import com.github.chriskn.structurizrextension.api.view.sprite.sprites.Sprite
import com.github.chriskn.structurizrextension.internal.export.view.style.spriteFromJson
import com.github.chriskn.structurizrextension.internal.export.view.style.toJson
import com.structurizr.model.ModelItem

private const val C4_PROPERTY_PREFIX = "additionalProperty:"
private const val C4_PROPERTY_HEADER_PREFIX = "additionalPropertyHeader"
private const val SPRITE_PROPERTY = "sprite"
private const val LINK_PROPERTY = "link"

var ModelItem.c4Properties: C4Properties?
    /**
     * Returns the [C4Properties] or null if not set.
     */
    get() {
        val headers = this.properties
            .filter { it.key.startsWith(C4_PROPERTY_HEADER_PREFIX) }
            .toList()
            .sortedBy { it.first.split(":").last() }
            .map { it.second }
            .ifEmpty { null }
        val values = this.properties
            .filter { it.key.startsWith(C4_PROPERTY_PREFIX) }
            .toList()
            .groupBy {
                val split = it.first.split(":")
                split[split.size - 2]
            }
            .toSortedMap()
            .map { columnsByRow ->
                columnsByRow.value
                    .sortedBy { it.first.split(":").last() }
                    .map { it.second }
            }
        return if (headers.isNullOrEmpty() && values.isEmpty()) null else C4Properties(headers, values)
    }

    /**
     * Sets the [C4Properties]. Properties are added to the existing properties.
     */
    set(properties) {
        if (properties != null) {
            properties.header?.forEachIndexed { index, headerValue ->
                this.addProperty("$C4_PROPERTY_HEADER_PREFIX:$index", headerValue)
            }
            properties.values.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { columnIndex, property ->
                    this.addProperty("$C4_PROPERTY_PREFIX:$rowIndex:$columnIndex", property)
                }
            }
        }
    }

var ModelItem.sprite: Sprite?
    /**
     * Returns the sprite or null if not set.
     */
    get() = this.properties[SPRITE_PROPERTY]?.let { spriteFromJson(it) }

    /**
     * Sets the sprite if not null or blank.
     */
    set(sprite) {
        if (sprite != null) {
            this.addProperty(SPRITE_PROPERTY, sprite.toJson())
        }
    }

var ModelItem.link: String?
    /**
     * Returns the link or null if not set.
     */
    get() = this.properties[LINK_PROPERTY]

    /**
     * Sets the link if not null or blank.
     */
    set(link) {
        if (!link.isNullOrBlank()) {
            this.addProperty(LINK_PROPERTY, link)
        }
    }

internal fun ModelItem.configure(
    sprite: Sprite?,
    link: String?,
    tags: List<String>,
    c4Properties: C4Properties?
) {
    this.sprite = sprite
    this.link = link
    tags.forEach { tag -> this.addTags(tag) }
    this.c4Properties = c4Properties
}
