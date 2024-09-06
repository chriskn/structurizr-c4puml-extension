@file:Suppress("TooManyFunctions")

package com.github.chriskn.structurizrextension.internal.export.view.style

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.chriskn.structurizrextension.api.view.style.styles.BoundaryStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.DependencyStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.ElementStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.PersonStyle

internal const val ELEMENT_STYLE_PROPERTY_NAME_PREFIX = "c4:elementStyle"
internal const val BOUNDARY_STYLE_PROPERTY_NAME_PREFIX = "c4:boundaryStyle"
internal const val PERSON_STYLE_PROPERTY_NAME_PREFIX = "c4:personStyle"
internal const val DEPENDENCY_STYLE_PROPERTY_NAME_PREFIX = "c4:dependencyStyle"

private val mapper = jacksonObjectMapper()

internal fun ElementStyle.toJson(): String = mapper.writeValueAsString(this)

internal fun ElementStyle.propertyName(): String = "$ELEMENT_STYLE_PROPERTY_NAME_PREFIX:${this.tag}"

internal fun elementStyleFromJson(json: String): ElementStyle = mapper.readValue(json)

internal fun BoundaryStyle.toJson(): String = mapper.writeValueAsString(this)

internal fun BoundaryStyle.propertyName(): String = "$BOUNDARY_STYLE_PROPERTY_NAME_PREFIX:${this.tag}"

internal fun boundaryStyleFromJson(json: String): BoundaryStyle = mapper.readValue(json)

internal fun PersonStyle.toJson(): String = mapper.writeValueAsString(this)

internal fun PersonStyle.propertyName(): String = "$PERSON_STYLE_PROPERTY_NAME_PREFIX:${this.tag}"

internal fun personStyleFromJson(json: String): PersonStyle = mapper.readValue(json)

internal fun DependencyStyle.toJson(): String = mapper.writeValueAsString(this)

internal fun DependencyStyle.propertyName(): String = "$DEPENDENCY_STYLE_PROPERTY_NAME_PREFIX:${this.tag}"

internal fun dependencyStyleFromJson(json: String): DependencyStyle = mapper.readValue(json)
