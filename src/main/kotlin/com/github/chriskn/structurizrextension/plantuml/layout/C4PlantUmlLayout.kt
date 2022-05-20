package com.github.chriskn.structurizrextension.plantuml.layout

import com.structurizr.model.Relationship

enum class LineType(val macro: String) {
    ORTHO("skinparam linetype ortho"),
    POLYLINE("skinparam linetype polyline")
}

enum class Layout(val macro: String) {
    TOP_DOWN("LAYOUT_TOP_DOWN()"),
    LEFT_TO_RIGHT("LAYOUT_LEFT_RIGHT()"),
    LANDSCAPE("LAYOUT_LANDSCAPE()")
}

enum class Legend(val macro: String) {
    SHOW_STATIC_LEGEND("LAYOUT_WITH_LEGEND"),
    SHOW_FLOATING_LEGEND("SHOW_FLOATING_LEGEND"),
    SHOW_LEGEND("SHOW_LEGEND"),
    NONE("")
}

enum class Direction {
    UP, DOWN, RIGHT, LEFT;

    fun macro() = this.name.first()
}

enum class Mode(val macro: String) {
    NEIGHBOR("Neighbor"),
    BACK("Back"),
    BACK_NEIGHBOR("Back_Neighbor"),
    REL("Rel")
}

data class DependencyConfiguration(
    val filter: (predicate: Relationship) -> Boolean,
    val mode: Mode? = null,
    val direction: Direction? = null
)

data class C4PlantUmlLayout(
    val nodeSep: Int? = null,
    val rankSep: Int? = null,
    val lineType: LineType? = null,
    val layout: Layout = Layout.TOP_DOWN,
    val legend: Legend = Legend.SHOW_LEGEND,
    val showPersonOutline: Boolean = true,
    val hideStereotypes: Boolean = true,
    val dependencyConfigurations: List<DependencyConfiguration> = listOf()
)
