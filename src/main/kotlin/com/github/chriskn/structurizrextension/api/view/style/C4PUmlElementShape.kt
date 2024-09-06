package com.github.chriskn.structurizrextension.api.view.style

/**
 * Enum representing the available element shapes in C4 PlantUml
 */
enum class C4PUmlElementShape(internal val pUmlString: String) {
    ROUNDED_BOX("RoundedBoxShape()"),
    EIGHT_SIDED("EightSidedShape()")
}
