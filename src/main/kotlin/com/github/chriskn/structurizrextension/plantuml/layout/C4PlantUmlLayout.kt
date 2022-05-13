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
    SHOW_STATIC_LEGEND("LAYOUT_WITH_LEGEND()"),
    SHOW_FLOATING_LEGEND("SHOW_FLOATING_LEGEND()"),
    SHOW_LEGEND("SHOW_LEGEND()")
}

enum class DependencyPosition {
    Up, Down, Right, Left
}

enum class DependencyMode {
    Neighbor, Back, Back_Neighbor, Rel
}

data class DependencyConfiguration(
    val filter: (predicate: Relationship) -> Boolean,
    val mode: DependencyMode? = null,
    val position: DependencyPosition? = null
)

data class C4PlantUmlLayout(
    val nodeSep: Int? = null,
    val rankSep: Int? = null,
    val lineType: LineType? = null,
    val layout: Layout = Layout.TOP_DOWN,
    val legend: Legend = Legend.SHOW_LEGEND,
    val showPersonOutline: Boolean = true,
    val dependencyConfigurations: List<DependencyConfiguration> = listOf()
)
