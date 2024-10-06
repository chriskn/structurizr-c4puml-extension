package com.github.chriskn.structurizrextension.api.view.style.styles

import com.github.chriskn.structurizrextension.api.view.style.C4Shape
import com.github.chriskn.structurizrextension.api.view.style.sprite.Sprite
import com.structurizr.view.Border

// TODO move to internal package
interface C4PumlStyle {
    val tag: String
    val backgroundColor: String?
    val fontColor: String?
    val border: Border?
    val borderWith: Int?
    val borderColor: String?
    val shadowing: Boolean
    val c4Shape: C4Shape?
    val sprite: Sprite?
    val legendText: String?
    val legendSprite: Sprite?
}
