package com.github.chriskn.structurizrextension.internal.export.view.style

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.structurizr.view.ElementStyle

private val mapper = jacksonObjectMapper()

internal fun ElementStyle.toJson(): String = mapper.writeValueAsString(this)

internal fun elementStyleFromJson(json: String): ElementStyle = mapper.readValue(json)
