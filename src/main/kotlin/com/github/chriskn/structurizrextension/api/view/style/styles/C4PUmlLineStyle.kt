package com.github.chriskn.structurizrextension.api.view.style.styles

/**
 * Enum representing the available line styles in C4 PlantUml
 */
enum class C4PUmlLineStyle(internal val pUmlString: String) {
    DASHED("DashedLine()"),
    DOTTED("DottedLine()"),
    BOLD("BoldLine()"),
    SOLID("SolidLine()"),
}
