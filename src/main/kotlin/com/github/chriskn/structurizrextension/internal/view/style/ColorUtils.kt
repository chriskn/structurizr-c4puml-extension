package com.github.chriskn.structurizrextension.internal.view.style

import com.structurizr.view.Color

internal fun toValidColor(color: String?): String? =
    if (color == null) {
        null
    } else if (Color.isHexColorCode(color)) {
        color.lowercase()
    } else {
        val hexColorCode = Color.fromColorNameToHexColorCode(color)
        requireNotNull(hexColorCode) { "$color is not a valid hex color code or HTML color name." }
        hexColorCode.lowercase()
    }
