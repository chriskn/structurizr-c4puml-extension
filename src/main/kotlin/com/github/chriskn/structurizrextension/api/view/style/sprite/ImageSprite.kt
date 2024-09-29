package com.github.chriskn.structurizrextension.api.view.style.sprite

import java.net.URI

private val fileInUriRegex = "[^/\\\\&?]+\\.\\w{3,4}(?=([?&].*\$|\$))".toRegex()

data class ImageSprite(
    val url: String,
    val scale: Double? = null,
) : Sprite(scale) {

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
