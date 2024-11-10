package com.github.chriskn.structurizrextension.internal.export.writer

import com.github.chriskn.structurizrextension.api.icons.IconRegistry
import com.github.chriskn.structurizrextension.api.model.icon
import com.github.chriskn.structurizrextension.api.model.sprite
import com.github.chriskn.structurizrextension.internal.export.writer.StyleWriter.toPlantUmlString
import com.structurizr.model.ModelItem
import com.structurizr.model.Relationship

internal fun ModelItem.getUsedIconOrSprite(): String {
    val iconStr = IconRegistry.iconFileNameFor(this.icon).orEmpty()
    val spriteStr = this.sprite?.toPlantUmlString().orEmpty()
    val usedSprite = if (spriteStr.isNotEmpty()) {
        "${'$'}sprite=$spriteStr"
    } else {
        """"$iconStr""""
    }
    return usedSprite
}

internal fun Relationship.getUsedIconOrSprite(): String {
    val iconStr = IconRegistry.iconFileNameFor(this.icon).orEmpty()
    val spriteStr = this.sprite?.toPlantUmlString().orEmpty()
    val usedSprite = if (spriteStr.isNotEmpty()) {
        "${'$'}sprite=$spriteStr"
    } else {
        iconStr
    }
    return usedSprite
}
