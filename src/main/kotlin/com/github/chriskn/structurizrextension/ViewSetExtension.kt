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

fun ViewSet.createSystemLandscapeView(
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null
): SystemLandscapeView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    return this.createSystemLandscapeView(key, description)
}

// TODO test
fun ViewSet.createSystemContextView(
    softwareSystem: SoftwareSystem,
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null
): SystemContextView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    return this.createSystemContextView(softwareSystem, key, description)
}

fun ViewSet.createContainerView(
    softwareSystem: SoftwareSystem,
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null
): ContainerView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    return this.createContainerView(softwareSystem, key, description)
}

fun ViewSet.createComponentView(
    container: Container,
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null
): ComponentView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    return this.createComponentView(container, key, description)
}

fun ViewSet.createDynamicView(
    container: Container,
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null
): DynamicView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    return this.createDynamicView(container, key, description)
}

// TODO test
fun ViewSet.createDynamicView(
    system: SoftwareSystem,
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null
): DynamicView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    return this.createDynamicView(system, key, description)
}

fun ViewSet.createDeploymentView(
    system: SoftwareSystem,
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null
): DeploymentView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    return this.createDeploymentView(system, key, description)
}

fun ViewSet.createDeploymentView(
    key: String,
    description: String,
    layout: C4PlantUmlLayout? = null
): DeploymentView {
    layout?.let { LayoutRegistry.registerLayoutForKey(key, layout) }
    return this.createDeploymentView(key, description)
}
