package com.github.chriskn.structurizrextension.api.model

import com.structurizr.model.Container
import com.structurizr.model.Location
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem

private const val LOCATION_PROPERTY = "c4location"

var SoftwareSystem.c4Location: Location
    /**
     * Returns the [Location] of the SoftwareSystem.
     */
    get() = Location.valueOf(this.properties.getValue(LOCATION_PROPERTY))

    /**
     * Sets the [Location] of the SoftwareSystem.
     */
    set(location) {
        this.addProperty(LOCATION_PROPERTY, location.name)
    }

var Container.c4Location: Location
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

var Person.c4Location: Location
    /**
     * Returns the [Location] of the Component.
     */
    get() = Location.valueOf(this.properties.getValue(LOCATION_PROPERTY))

    /**
     * Sets the [Location] of the Component.
     */
    set(location) {
        this.addProperty(LOCATION_PROPERTY, location.name)
    }
