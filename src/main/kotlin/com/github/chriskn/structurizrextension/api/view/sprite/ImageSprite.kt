package com.github.chriskn.structurizrextension.api.view.sprite

import java.net.URI

private val fileInUriRegex = "[^/\\\\&?]+\\.\\w{3,4}(?=([?&].*\$|\$))".toRegex()

/**
 * Image sprite
 *
 * Can be used to include images via an image url.
 *
 * @property name                   the name of the sprite. can be null
 * @property url                    the image url. Must use "img:" scheme and point to a file. img:{File or Url}
 * @property scale                  the image scale. Must be greater zero. 1.0 is default
 * @property additionalDefinitions  each will be written as !define <additionalDefinition> in the output file.
 *                                  Can be used to define named urls. The names then can be used in the url
 * @constructor Create Image sprite
 */
data class ImageSprite(
    val url: String,
    val scale: Double? = null,
    override val name: String? = null,
    val additionalDefinitions: List<String>? = null,
) : Sprite(name, scale) {

    init {
        validateImageUrl(url)
    }

    private fun validateImageUrl(urlString: String) {
        val uri = URI(urlString)
        val mathResult = fileInUriRegex.find(uri.schemeSpecificPart)
        val fileWithEnding = mathResult?.groupValues?.first()
        require(fileWithEnding?.contains(".") == true) {
            "Image URI must point to a file"
        }
        require(uri.scheme == "img") { "Image URI must use img scheme" }
    }
}
