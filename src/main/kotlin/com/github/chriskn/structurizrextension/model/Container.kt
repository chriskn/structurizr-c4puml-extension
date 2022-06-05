package com.github.chriskn.structurizrextension.model

import com.structurizr.io.plantuml.C4PlantUMLWriter
import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.Location
import com.structurizr.model.StaticStructureElement

const val LOCATION_PROPERTY = "c4location"

/**
 * Adds a component to this container.
 *
 * @param name          the name of the component
 * @param description   the description of the component
 * @param icon          the icon of the component. See IconRegistry for available icons or add your own
 * @param link          the link of the component
 * @param technology    the technology of the component
 * @param tags          the list of tags of the component
 * @param properties    [C4Properties] of the component
 * @param uses          the list of [Dependency] to a system, container or component the component uses. A person can't be used
 * @param usedBy        the list of [Dependency] to a system, container, component or person the component is used by
 * @return the resulting Component
 * @throws IllegalArgumentException if a component with the same name already exists or a person is used in an uses dependency
 */
@Suppress("LongParameterList")
fun Container.component(
    name: String,
    description: String,
    icon: String? = null,
    link: String? = null,
    technology: String = "",
    tags: List<String> = listOf(),
    properties: C4Properties? = null,
    uses: List<Dependency<StaticStructureElement>> = listOf(),
    usedBy: List<Dependency<StaticStructureElement>> = listOf()
): Component {
    val component = this.addComponent(name, description, technology)
    component.configure(icon, link, tags, properties, uses, usedBy)
    return component
}

var Container.type: C4Type?
    /**
     * Returns the [C4Type] of the container.
     */
    get() = if (this.properties.containsKey(C4PlantUMLWriter.C4_ELEMENT_TYPE)) {
        C4Type.fromC4Type(this.properties.getValue(C4PlantUMLWriter.C4_ELEMENT_TYPE))
    } else {
        null
    }
    /**
     * Sets the [C4Type] of the container.
     */
    set(value) {
        if (value != null) {
            this.addProperty(C4PlantUMLWriter.C4_ELEMENT_TYPE, value.c4Type)
        }
    }

var Container.location: Location
    /**
     * Returns the [Location] of the container.
     */
    get() = Location.valueOf(this.properties.getValue(LOCATION_PROPERTY))
    /**
     * Sets the [Location] of the container.
     */
    set(location) {
        this.addProperty(LOCATION_PROPERTY, location.name)
    }
