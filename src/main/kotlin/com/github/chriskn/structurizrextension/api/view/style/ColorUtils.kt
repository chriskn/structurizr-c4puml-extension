package com.github.chriskn.structurizrextension.api.view.style

import com.structurizr.view.Color

fun toValidColor(color: String): String =
    if (Color.isHexColorCode(color)) {
        color.lowercase()
    } else {
        val hexColorCode = Color.fromColorNameToHexColorCode(color)
        requireNotNull(hexColorCode) { "$color is not a valid hex color code or HTML color name." }
        hexColorCode.lowercase()
    }
