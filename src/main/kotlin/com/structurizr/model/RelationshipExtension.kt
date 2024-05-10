package com.structurizr.model

internal fun Relationship.removeProperty(key: String) {
    this.properties = this.properties.filterKeys { it != key }
}
