package com.github.chriskn.structurizrextension.model

import com.structurizr.model.Element

private const val C4_PROPERTY_PREFIX = "additionalProperty:"
private const val C4_PROPERTY_HEADER_PREFIX = "additionalPropertyHeader"
private const val ICON_PROPERTY = "icon"
private const val LINK_PROPERTY = "link"

fun Element.configure(
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

var Element.c4Properties: C4Properties?
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
        return if (headers.isNullOrEmpty() && values.isNullOrEmpty()) null else C4Properties(headers, values)
    }
    set(properties) {
        if (properties != null) {
            properties.headers?.forEachIndexed { index, headerValue ->
                this.addProperty("$C4_PROPERTY_HEADER_PREFIX:$index", headerValue)
            }
            properties.values.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { columnIndex, property ->
                    this.addProperty("$C4_PROPERTY_PREFIX:$rowIndex:$columnIndex", property)
                }
            }
        }
    }

var Element.icon: String?
    get() = this.properties[ICON_PROPERTY]
    set(icon) {
        if (icon != null) {
            this.addProperty(ICON_PROPERTY, icon)
        }
    }

var Element.link: String?
    get() = this.properties[LINK_PROPERTY]
    set(link) {
        if (link != null) {
            this.addProperty(LINK_PROPERTY, link)
        }
    }
