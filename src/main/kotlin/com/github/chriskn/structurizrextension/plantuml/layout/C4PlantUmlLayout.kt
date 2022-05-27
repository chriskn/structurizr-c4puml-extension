package com.github.chriskn.structurizrextension.plantuml.layout

import com.structurizr.model.Relationship

enum class LineType(val macro: String) {
    Ortho("skinparam linetype ortho"),
    Polyline("skinparam linetype polyline")
}

enum class Layout(val macro: String) {
    TopDown("LAYOUT_TOP_DOWN()"),
    LeftToRight("LAYOUT_LEFT_RIGHT()"),
    Landscape("LAYOUT_LANDSCAPE()")
}

enum class Legend(val macro: String) {
    ShowStaticLegend("LAYOUT_WITH_LEGEND"),
    ShowFloatingLegend("SHOW_FLOATING_LEGEND"),
    ShowLegend("SHOW_LEGEND"),
    None("")
}

enum class Mode(val macro: String) {
    Neighbor("Neighbor"),
    Back("Back"),
    BackNeighbor("Back_Neighbor"),
    Rel("Rel")
}

enum class Direction {
    Up, Down, Right, Left;

    fun macro() = this.name.first()
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
    val layout: Layout = Layout.TopDown,
    val legend: Legend = Legend.ShowLegend,
    val showPersonOutline: Boolean = true,
    val hideStereotypes: Boolean = true,
    val dependencyConfigurations: List<DependencyConfiguration> = listOf()
)
