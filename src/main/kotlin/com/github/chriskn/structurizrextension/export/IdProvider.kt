package com.github.chriskn.structurizrextension.export

import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.ContainerInstance
import com.structurizr.model.DeploymentNode
import com.structurizr.model.Element
import com.structurizr.model.InfrastructureNode
import com.structurizr.model.ModelItem
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.model.SoftwareSystemInstance


fun idOf(modelItem: ModelItem): String {
    if (modelItem is Element) {
        val isRootModelItem = modelItem.parent == null
        return if (isRootModelItem) {
            if (modelItem is DeploymentNode) {
                escape(modelItem.environment) + "." + id(modelItem)
            } else {
                id(modelItem)
            }
        } else {
            idOf(modelItem.parent) + "." + id(modelItem)
        }
    }
    return id(modelItem)
}

private fun id(modelItem: ModelItem): String = when (modelItem) {
    is Person -> id(modelItem)
    is SoftwareSystem -> id(modelItem)
    is Container -> id(modelItem)
    is Component -> id(modelItem)
    is DeploymentNode -> id(modelItem)
    is InfrastructureNode -> id(modelItem)
    is SoftwareSystemInstance -> id(modelItem)
    is ContainerInstance -> id(modelItem)
    else -> modelItem.id
}

private fun id(person: Person): String =
    escape(person.name)

private fun id(softwareSystem: SoftwareSystem): String =
    escape(softwareSystem.name)

private fun id(container: Container): String =
   escape(container.name)

private fun id(component: Component): String =
    escape(component.name)

private fun id(deploymentNode: DeploymentNode): String =
    escape(deploymentNode.name)

private fun id(infrastructureNode: InfrastructureNode): String =
    escape(infrastructureNode.name)

private fun id(softwareSystemInstance: SoftwareSystemInstance): String =
    escape(softwareSystemInstance.name) + "_" + softwareSystemInstance.instanceId

private fun id(containerInstance: ContainerInstance): String =
    escape(containerInstance.name) + "_" + containerInstance.instanceId

private val idEscapeRegex = "(?U)\\W".toRegex()

private fun escape(s: String): String = s.replace(idEscapeRegex, "")

