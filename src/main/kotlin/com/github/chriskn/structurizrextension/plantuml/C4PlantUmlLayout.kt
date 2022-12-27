package com.github.chriskn.structurizrextension.plantuml

import com.github.chriskn.structurizrextension.plantuml.Direction.Down
import com.github.chriskn.structurizrextension.plantuml.Direction.Left
import com.github.chriskn.structurizrextension.plantuml.Direction.Right
import com.github.chriskn.structurizrextension.plantuml.Direction.Up
import com.github.chriskn.structurizrextension.plantuml.Layout.Landscape
import com.github.chriskn.structurizrextension.plantuml.Layout.LeftToRight
import com.github.chriskn.structurizrextension.plantuml.Layout.TopDown
import com.github.chriskn.structurizrextension.plantuml.Legend.None
import com.github.chriskn.structurizrextension.plantuml.Legend.ShowFloatingLegend
import com.github.chriskn.structurizrextension.plantuml.Legend.ShowLegend
import com.github.chriskn.structurizrextension.plantuml.Legend.ShowStaticLegend
import com.github.chriskn.structurizrextension.plantuml.LineType.Ortho
import com.github.chriskn.structurizrextension.plantuml.LineType.Polyline
import com.github.chriskn.structurizrextension.plantuml.Mode.Back
import com.github.chriskn.structurizrextension.plantuml.Mode.BackNeighbor
import com.github.chriskn.structurizrextension.plantuml.Mode.Neighbor
import com.github.chriskn.structurizrextension.plantuml.Mode.Rel
import com.structurizr.model.Relationship

/**
 * Options for the PlanUML skinparam linetype.
 *
 * * [Ortho]    represents PlanUML skinparam linetype ortho
 * * [Polyline] represents PlanUML skinparam linetype polyline
 */
enum class LineType(val macro: String) {
    Ortho("skinparam linetype ortho"),
    Polyline("skinparam linetype polyline")
}

/**
 * Options for the C4-PlantUML layout.
 *
 * * [TopDown]      represents C4-PlantUML LAYOUT_TOP_DOWN() layout
 * * [LeftToRight]  represents C4-PlantUML LAYOUT_LEFT_RIGHT() layout (rotates dependency direction)
 * * [Landscape]    represents C4-PlantUML LAYOUT_LANDSCAPE() layout (like LAYOUT_LEFT_RIGHT but without dependency direction rotation)
 */
enum class Layout(val macro: String) {
    TopDown("LAYOUT_TOP_DOWN()"),
    LeftToRight("LAYOUT_LEFT_RIGHT()"),
    Landscape("LAYOUT_LANDSCAPE()")
}

/**
 * Options for the C4-PlantUML legend.
 *
 * * [ShowStaticLegend]     represents C4-PlantUML LAYOUT_WITH_LEGEND
 * * [ShowFloatingLegend]   represents C4-PlantUML SHOW_FLOATING_LEGEND
 * * [ShowLegend]           represents C4-PlantUML SHOW_LEGEND
 * * [None]                 no legend will be added
 */
enum class Legend(val macro: String) {
    ShowStaticLegend("LAYOUT_WITH_LEGEND"),
    ShowFloatingLegend("SHOW_FLOATING_LEGEND"),
    ShowLegend("SHOW_LEGEND"),
    None("")
}

/**
 * Options for the dependency layout configuration in order to control element positioning.
 *
 * * [Neighbor]     elements of the dependency will be placed adjacent
 * * [Back]         dependency direction will be inverted
 * * [BackNeighbor] elements of the dependency will be placed adjacent with inverted dependency direction
 * * [Rel]          default dependency layout
 * * [RelIndex]     only used for DynamicView
 */
enum class Mode(val macro: String) {
    Neighbor("Neighbor"),
    Back("Back"),
    BackNeighbor("Back_Neighbor"),
    Rel("Rel"),
    RelIndex("RelIndex")
}

/**
 * Options for the dependency direction.
 *
 * See [Layout] for how the chosen layout influences the dependency direction.
 *
 * * [Up]       dependency direction is up.
 * * [Down]     dependency direction is down.
 * * [Right]    dependency direction is right.
 * * [Left]     dependency direction is left.
 */
enum class Direction {
    Up, Down, Right, Left;

    fun macro() = this.name.first()
    fun inverse(): Direction = when (this) {
        Up -> Down
        Down -> Up
        Left -> Right
        Right -> Left
    }
}

/**
 * Allows the layout configuration for specific dependencies and therefore the position of the related elements.
 *
 * In order to configure a dependency the related [Relationship] must be determined
 *
 * @param filter    the filter determining for which [Relationship]s the configuration should be applied
 * @param mode      the [Mode] that should be applied for the filtered relationships
 * @param direction the [Direction] that should be applied for the filtered relationships
 */
data class DependencyConfiguration(
    val filter: (predicate: Relationship) -> Boolean,
    val mode: Mode? = null,
    val direction: Direction? = null
)

/**
 * Allows the configuration of the layout for generated C4-PlantUML diagram.
 *
 * @param nodeSep                   the PlantUML nodesep skinparam in order to control the distance between nodes
 * @param rankSep                   the PlantUML ranksep skinparam in order to control the distance between ranks
 * @param lineType                  the PlantUML [LineType] skinparam
 * @param layout                    the C4PlantUML [Layout]. [TopDown] is default
 * @param layout                    the C4PlantUML [Legend]. [ShowLegend] is default
 * @param showPersonOutline         activates person outline instead of a rectangle (default is true)
 * @param hideStereotypes           hides stereotypes when rendering elements (default is true)
 * @param dependencyConfigurations  list of [DependencyConfiguration]
 */
data class C4PlantUmlLayout(
    val nodeSep: Int? = null,
    val rankSep: Int? = null,
    val lineType: LineType? = null,
    val layout: Layout = TopDown,
    val legend: Legend = ShowLegend,
    val showPersonOutline: Boolean = true,
    val hideStereotypes: Boolean = true,
    val dependencyConfigurations: List<DependencyConfiguration> = listOf()
)
