package com.github.chriskn.structurizrextension.api.view.style

import java.net.URI

// TODO doucment
class Sprite {

    private val imageUri: URI?
    private val openIconicIconName: String?
    private val scale: Double?
    private val color: String?

    companion object {

        private val paramsRegex = "\\{(.*)}".toRegex()
        private val fileInUriRegex = "[^/\\\\&?]+\\.\\w{3,4}(?=([?&].*\$|\$))".toRegex()

        internal fun fromString(value: String): Sprite {
            val params = paramsRegex.find(value)?.groupValues?.last()
            val scale = extractScale(params)
            val color = extractColor(params)
            val iconPath = if (value.contains('{')) {
                value.substring(0, value.indexOfFirst { it == '{' })
            } else {
                value
            }
            val isOpenIconicIcon = iconPath.startsWith("&")
            if (isOpenIconicIcon) {
                val openIconicIconName = iconPath.substring(1)
                return Sprite(openIconicIconName, scale, color)
            } else {
                val imageUri = URI.create(iconPath)
                return Sprite(imageUri, scale)
            }
        }

        private fun extractScale(paramsString: String?): Double? {
            val scaleValue = extractParamValue(paramsString, "scale")
            return scaleValue?.toDouble()
        }

        private fun extractColor(paramsString: String?): String? {
            val colorValue = extractParamValue(paramsString, "color")
            return colorValue
        }

        private fun extractParamValue(paramsString: String?, paramName: String) =
            paramsString?.split(",")?.firstOrNull { it.startsWith(paramName) }?.split("=")?.lastOrNull()
    }

    constructor(
        imageUri: URI,
        scale: Double? = null,
    ) {
        validateScale(scale)
        validateImageUri(imageUri)
        this.imageUri = imageUri
        this.scale = scale
        this.color = null
        this.openIconicIconName = null
    }

    // https://www.plantuml.com/plantuml/uml/SoWkIImgAStDuSh9B2x9BqZDoqpE1s8kXzIy590m0000
    constructor(
        openIconicIconName: String,
        scale: Double? = null,
        color: String? = null,
    ) {
        validateScale(scale)
        this.scale = scale
        this.color = color?.let { toValidColor(it) }
        this.openIconicIconName = openIconicIconName
        this.imageUri = null
    }

    private fun validateScale(scale: Double?) {
        require(scale == null || scale > 0) {
            throw IllegalArgumentException("Sprite scale can not be negative")
        }
    }

    private fun validateImageUri(imageUri: URI) {
        val mathResult = fileInUriRegex.find(imageUri.schemeSpecificPart)
        val fileWithEnding = mathResult?.groupValues?.first()
        require(fileWithEnding?.contains(".") == true) {
            "Image URI must point to a file"
        }
        require(imageUri.scheme == "img") { "Image URI must have a valid img scheme" }
    }

    override fun toString(): String {
        val spritePath = imageUri?.toString() ?: "&$openIconicIconName"
        val scale = if (scale != null) {
            "scale=$scale"
        } else {
            ""
        }
        val color = if (color != null) {
            "color=$color"
        } else {
            ""
        }
        return if (scale.isBlank() && color.isBlank()) {
            spritePath
        } else if (color.isBlank()) {
            "$spritePath{$scale}"
        } else if (scale.isBlank()) {
            "$spritePath{$color}"
        } else {
            "$spritePath{$scale,$color}"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sprite

        if (imageUri != other.imageUri) return false
        if (openIconicIconName != other.openIconicIconName) return false
        if (scale != other.scale) return false
        if (color != other.color) return false

        return true
    }

    override fun hashCode(): Int {
        var result = imageUri?.hashCode() ?: 0
        result = 31 * result + (openIconicIconName?.hashCode() ?: 0)
        result = 31 * result + (scale?.hashCode() ?: 0)
        result = 31 * result + (color?.hashCode() ?: 0)
        return result
    }
}
