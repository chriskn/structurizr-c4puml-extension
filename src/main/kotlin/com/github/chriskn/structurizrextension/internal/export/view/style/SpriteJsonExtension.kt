@file:Suppress("TooManyFunctions")

package com.github.chriskn.structurizrextension.internal.export.view.style

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.chriskn.structurizrextension.api.view.sprite.Sprite

private val mapper = jacksonObjectMapper()

internal fun Sprite.toJson(): String = mapper.writeValueAsString(this)

internal fun spriteFromJson(json: String): Sprite = mapper.readValue(json)
