package com.github.chriskn.structurizrextension

import com.github.chriskn.structurizrextension.plantuml.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.plantuml.layout.LayoutRegistry
import com.structurizr.model.Container
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ComponentView
import com.structurizr.view.ContainerView
import com.structurizr.view.DeploymentView
import com.structurizr.view.DynamicView
import com.structurizr.view.SystemContextView
import com.structurizr.view.SystemLandscapeView
import com.structurizr.view.ViewSet

fun ViewSet.systemLandscapeView(
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null
): SystemLandscapeView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    return this.createSystemLandscapeView(key, description)
}

fun ViewSet.systemContextView(
    softwareSystem: SoftwareSystem,
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null
): SystemContextView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    return this.createSystemContextView(softwareSystem, key, description)
}

fun ViewSet.containerView(
    softwareSystem: SoftwareSystem,
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null
): ContainerView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    return this.createContainerView(softwareSystem, key, description)
}

fun ViewSet.componentView(
    container: Container,
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null
): ComponentView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    return this.createComponentView(container, key, description)
}

fun ViewSet.dynamicView(
    container: Container,
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null
): DynamicView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    return this.createDynamicView(container, key, description)
}

fun ViewSet.dynamicView(
    system: SoftwareSystem,
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null
): DynamicView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    return this.createDynamicView(system, key, description)
}

fun ViewSet.deploymentView(
    system: SoftwareSystem,
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null
): DeploymentView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    return this.createDeploymentView(system, key, description)
}
