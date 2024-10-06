package com.github.chriskn.structurizrextension.internal.export.view.style

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.chriskn.structurizrextension.api.view.style.styles.BoundaryStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.ElementStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.PersonStyle

private val mapper = jacksonObjectMapper()

internal fun ElementStyle.toJson(): String = mapper.writeValueAsString(this)

internal fun elementStyleFromJson(json: String): ElementStyle = mapper.readValue(json)

internal fun BoundaryStyle.toJson(): String = mapper.writeValueAsString(this)

internal fun boundaryStyleFromJson(json: String): BoundaryStyle = mapper.readValue(json)

internal fun PersonStyle.toJson(): String = mapper.writeValueAsString(this)

internal fun personStyleFromJson(json: String): PersonStyle = mapper.readValue(json)
