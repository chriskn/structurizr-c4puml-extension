package com.github.chriskn.structurizrextension.model

import com.structurizr.model.ModelItem

private const val C4_PROPERTY_PREFIX = "additionalProperty:"
private const val C4_PROPERTY_HEADER_PREFIX = "additionalPropertyHeader"
private const val ICON_PROPERTY = "icon"
private const val LINK_PROPERTY = "link"

internal fun ModelItem.configure(
    icon: String?,
    link: String?,
    tags: List<String>,
    c4Properties: C4Properties?
) {
    this.icon = icon
    this.link = link
    tags.forEach { tag -> this.addTags(tag) }
    this.c4Properties = c4Properties
}

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
            .map { columnsByRow ->
                columnsByRow.value
                    .sortedBy { it.first.split(":").last() }
                    .map { it.second }
            }
        return if (headers.isNullOrEmpty() && values.isEmpty()) null else C4Properties(headers, values)
    }
    /**
     * Sets the [C4Properties].
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

var ModelItem.icon: String?
    /**
     * Returns the icon or null if not set.
     */
    get() = this.properties[ICON_PROPERTY]
    /**
     * Sets the icon.
     */
    set(icon) {
        if (icon != null) {
            this.addProperty(ICON_PROPERTY, icon)
        }
    }

var ModelItem.link: String?
    /**
     * Returns the link or null if not set.
     */
    get() = this.properties[LINK_PROPERTY]
    /**
     * Sets the link.
     */
    set(link) {
        if (link != null) {
            this.addProperty(LINK_PROPERTY, link)
        }
    }
