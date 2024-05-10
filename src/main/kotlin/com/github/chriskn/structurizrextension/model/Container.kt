package com.github.chriskn.structurizrextension.model

import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.StaticStructureElement

/**
 * Adds a component to this container.
 *
 * @param name          the name of the component
 * @param description   the description of the component
 * @param c4Type        the [C4Type] of the component
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
    c4Type: C4Type? = null,
    icon: String? = null,
    link: String? = null,
    technology: String = "",
    tags: List<String> = listOf(),
    properties: C4Properties? = null,
    uses: List<Dependency<StaticStructureElement>> = listOf(),
    usedBy: List<Dependency<StaticStructureElement>> = listOf()
): Component {
    val component = this.addComponent(name, description, technology)
    component.c4Type = c4Type
    component.configure(icon, link, tags, properties, uses, usedBy)
    return component
}
