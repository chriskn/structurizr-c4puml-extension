package com.github.chriskn.structurizrextension.internal.export.writer

import com.github.chriskn.structurizrextension.api.model.sprite
import com.github.chriskn.structurizrextension.internal.export.writer.StyleWriter.toPlantUmlString
import com.structurizr.model.ModelItem
import com.structurizr.model.Relationship

internal fun ModelItem.spriteString(): String {
    val spriteStr = this.sprite?.toPlantUmlString().orEmpty()
    val usedSprite = if (spriteStr.isNotBlank()) {
        ", ${'$'}sprite=$spriteStr"
    } else {
        ""
    }
    return usedSprite
}

internal fun Relationship.spriteString(): String {
    val spriteStr = this.sprite?.toPlantUmlString().orEmpty()
    val usedSprite = if (spriteStr.isNotBlank()) {
        ", ${'$'}sprite=$spriteStr"
    } else {
        ""
    }
    return usedSprite
}
