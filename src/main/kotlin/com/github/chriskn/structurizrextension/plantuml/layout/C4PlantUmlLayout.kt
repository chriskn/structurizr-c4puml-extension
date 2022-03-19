package com.github.chriskn.structurizrextension.plantuml.layout

enum class LineType(val macro: String) {
    ORTHO("skinparam linetype ortho"),
    POLYLINE("skinparam linetype polyline")
}

enum class Direction(val macro: String) {
    TOP_DOWN("LAYOUT_TOP_DOWN()"),
    LEFT_TO_RIGHT("LAYOUT_LEFT_RIGHT()")
}

enum class Legend(val macro: String) {
    SHOW_STATIC_LEGEND("LAYOUT_WITH_LEGEND()"),
    SHOW_FLOATING_LEGEND("SHOW_FLOATING_LEGEND()"),
    SHOW_LEGEND("SHOW_LEGEND()")
}

data class C4PlantUmlLayout(
    val nodeSep: Int? = null,
    val rankSep: Int? = null,
    val lineType: LineType? = null,
    val direction: Direction = Direction.TOP_DOWN,
    val legend: Legend = Legend.SHOW_LEGEND,
    val showPersonOutline: Boolean = true
)
