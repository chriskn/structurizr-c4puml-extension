package com.github.chriskn.structurizrextension.api.view

import com.github.chriskn.structurizrextension.api.view.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.api.view.layout.LayoutRegistry
import com.structurizr.model.Container
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ComponentView
import com.structurizr.view.ContainerView
import com.structurizr.view.DeploymentView
import com.structurizr.view.DynamicView
import com.structurizr.view.SystemContextView
import com.structurizr.view.SystemLandscapeView
import com.structurizr.view.ViewSet

/**
 * Creates a system landscape view.
 *
 * @param key           the key for the view (must be unique)
 * @param description   a description of the view
 * @param layout        [C4PlantUmlLayout]
 * @return a SystemLandscapeView object
 * @throws IllegalArgumentException if the key is not unique
 */
fun ViewSet.systemLandscapeView(
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null,
): SystemLandscapeView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    val view = this.createSystemLandscapeView(key, description)
    // default
    view.showEnterpriseBoundary = false
    return view
}

/**
 * Creates a system context view, where the scope of the view is the specified software system.
 *
 * @param softwareSystem    the SoftwareSystem object representing the scope of the view
 * @param key               the key for the view (must be unique)
 * @param description       a description of the view
 * @param layout            [C4PlantUmlLayout]
 * @return a SystemContextView object
 * @throws IllegalArgumentException if the software system is null or the key is not unique
 */
fun ViewSet.systemContextView(
    softwareSystem: SoftwareSystem,
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null,
): SystemContextView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    val view = this.createSystemContextView(softwareSystem, key, description)
    // default
    view.showEnterpriseBoundary = false
    return view
}

/**
 * Creates a container view, where the scope of the view is the specified software system.
 *
 * @param system        the SoftwareSystem object representing the scope of the view
 * @param key           the key for the view (must be unique)
 * @param description   a description of the view
 * @param layout        [C4PlantUmlLayout]
 * @return a ContainerView object
 * @throws IllegalArgumentException if the software system is null or the key is not unique
 */
fun ViewSet.containerView(
    system: SoftwareSystem,
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null,
): ContainerView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    return this.createContainerView(system, key, description)
}

/**
 * Creates a component view, where the scope of the view is the specified container.
 *
 * @param container     the Container object representing the scope of the view
 * @param key           the key for the view (must be unique)
 * @param description   a description of the view
 * @param layout        [C4PlantUmlLayout]
 * @return a ContainerView object
 * @throws IllegalArgumentException if the container is null or the key is not unique
 */
fun ViewSet.componentView(
    container: Container,
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null,
): ComponentView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    return this.createComponentView(container, key, description)
}

/**
 * Creates a dynamic view, where the scope is the specified container.
 *
 * @param container     the Container object representing the scope of the view
 * @param key           the key for the view (must be unique)
 * @param description   a description of the view
 * @param layout        [C4PlantUmlLayout]
 * @return a DynamicView object
 * @throws IllegalArgumentException if the container is null or the key is not unique
 */
fun ViewSet.dynamicView(
    container: Container,
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null,
): DynamicView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    return this.createDynamicView(container, key, description)
}

/**
 * Creates a dynamic view, where the scope is the specified software system.
 *
 * @param system        the SoftwareSystem object representing the scope of the view
 * @param key           the key for the view (must be unique)
 * @param description   a description of the view
 * @param layout        [C4PlantUmlLayout]
 * @return a DynamicView object
 * @throws IllegalArgumentException if the software system is null or the key is not unique
 */
fun ViewSet.dynamicView(
    system: SoftwareSystem,
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null,
): DynamicView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    return this.createDynamicView(system, key, description)
}

/**
 * Creates a deployment view, where the scope of the view is the specified software system.
 *
 * @param system        the SoftwareSystem object representing the scope of the view
 * @param key           the key for the deployment view (must be unique)
 * @param description   a description of the view
 * @param layout        [C4PlantUmlLayout]
 * @return a DeploymentView object
 * @throws IllegalArgumentException if the software system is null or the key is not unique
 */
fun ViewSet.deploymentView(
    system: SoftwareSystem,
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null,
): DeploymentView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    return this.createDeploymentView(system, key, description)
}
