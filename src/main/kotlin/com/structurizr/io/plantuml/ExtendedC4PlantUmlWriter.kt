package com.structurizr.io.plantuml

import com.github.chriskn.structurizrextension.model.c4Properties
import com.github.chriskn.structurizrextension.model.icon
import com.github.chriskn.structurizrextension.model.link
import com.github.chriskn.structurizrextension.model.location
import com.github.chriskn.structurizrextension.model.type
import com.github.chriskn.structurizrextension.plantuml.AWS_ICON_COMMONS
import com.github.chriskn.structurizrextension.plantuml.AWS_ICON_URL
import com.github.chriskn.structurizrextension.plantuml.IconRegistry
import com.github.chriskn.structurizrextension.plantuml.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.plantuml.layout.DependencyConfiguration
import com.github.chriskn.structurizrextension.plantuml.layout.Direction
import com.github.chriskn.structurizrextension.plantuml.layout.Legend
import com.github.chriskn.structurizrextension.plantuml.layout.Mode
import com.github.chriskn.structurizrextension.view.LayoutRegistry
import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.ContainerInstance
import com.structurizr.model.DeploymentElement
import com.structurizr.model.DeploymentNode
import com.structurizr.model.Element
import com.structurizr.model.InfrastructureNode
import com.structurizr.model.InteractionStyle
import com.structurizr.model.Location
import com.structurizr.model.ModelItem
import com.structurizr.model.Person
import com.structurizr.model.Relationship
import com.structurizr.model.SoftwareSystem
import com.structurizr.model.SoftwareSystemInstance
import com.structurizr.view.ComponentView
import com.structurizr.view.ContainerView
import com.structurizr.view.DeploymentView
import com.structurizr.view.DynamicView
import com.structurizr.view.RelationshipView
import com.structurizr.view.SystemContextView
import com.structurizr.view.SystemLandscapeView
import com.structurizr.view.View
import java.io.Writer
import java.net.URI

private const val C4_PLANT_UML_STDLIB_URL = "https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master"

@Suppress("TooManyFunctions")
class ExtendedC4PlantUmlWriter : C4PlantUMLWriter() {

    private val separator = System.lineSeparator()

    private val c4IncludeForView = mapOf(
        DynamicView::class.java to "C4_Dynamic.puml",
        DeploymentView::class.java to "C4_Deployment.puml",
        ComponentView::class.java to "C4_Component.puml",
        ContainerView::class.java to "C4_Container.puml",
        SystemLandscapeView::class.java to "C4_Context.puml",
        SystemContextView::class.java to "C4_Context.puml"
    )

    override fun writeHeader(view: View, writer: Writer) {
        includes.clear()
        clearSkinParams()
        addIncludeUrls(view)
        super.writeHeader(view, writer)
        val layout = LayoutRegistry.layoutForKey(view.key)
        if (layout.showPersonOutline) {
            writer.write("SHOW_PERSON_OUTLINE()")
            writer.write(separator)
        }
        writer.write(layout.layout.macro)
        writer.write(separator + separator)
        if (view.relationships.any { it.relationship.interactionStyle == InteractionStyle.Asynchronous }) {
            writeAsyncRelTag(writer)
        }
    }

    private fun addIncludeUrls(view: View) {
        val elements: MutableSet<ModelItem> = view.elements.map { it.element }.toMutableSet()
        if (view is DeploymentView) {
            val children = elements
                .filterIsInstance<DeploymentNode>()
                .flatMap { collectElements(it, elements) }
            elements.addAll(children)
        }
        elements += view.relationships.map { it.relationship }
        val spriteIncludesForElements = elements
            .asSequence()
            .mapNotNull { it.icon?.let { technology -> IconRegistry.iconUrlFor(technology) } }
            .toSet()
            .toList()
            .sorted()
            .toMutableList()
        if (spriteIncludesForElements.any { it.startsWith(AWS_ICON_URL) }) {
            spriteIncludesForElements.add(0, AWS_ICON_COMMONS)
        }
        spriteIncludesForElements.forEach { addIncludeURL(URI(it)) }
        val c4PumlIncludeURI = URI("$C4_PLANT_UML_STDLIB_URL/${c4IncludeForView[view.javaClass]}")
        addIncludeURL(c4PumlIncludeURI)
    }

    private fun collectElements(deploymentNode: DeploymentNode, elements: MutableSet<ModelItem>): MutableSet<ModelItem> {
        elements.addAll(deploymentNode.infrastructureNodes)
        elements.addAll(deploymentNode.softwareSystemInstances.map { it.softwareSystem })
        elements.addAll(deploymentNode.containerInstances.map { it.container })
        deploymentNode.children.forEach { collectElements(it, elements) }
        return elements
    }

    override fun write(view: View, element: Element, writer: Writer, indent: Int) {
        val stringIdent = calculateIndent(indent)
        writeProperties(element, stringIdent, writer)
        when (element) {
            is DeploymentElement -> writeDeploymentElement(view, element, writer, stringIdent)
            is Container -> writer.write(element.toMacroString(element.id, stringIdent))
            is SoftwareSystem -> writer.write(element.toMacroString(element.id, stringIdent))
            is Person -> writer.write(element.toMacroString(stringIdent))
            is Component -> writer.write(element.toMacroString(stringIdent))
            else -> super.write(view, element, writer, indent)
        }
    }

    private fun writeDeploymentElement(
        view: View,
        deploymentElement: DeploymentElement,
        writer: Writer,
        stringIdent: String
    ) {
        if (view.isElementInView(deploymentElement)) {
            when (deploymentElement) {
                is ContainerInstance -> {
                    writeProperties(deploymentElement.container, stringIdent, writer)
                    writer.write(deploymentElement.container.toMacroString(deploymentElement.id, stringIdent))
                }
                is SoftwareSystemInstance -> {
                    writeProperties(deploymentElement.softwareSystem, stringIdent, writer)
                    writer.write(deploymentElement.softwareSystem.toMacroString(deploymentElement.id, stringIdent))
                }
                is InfrastructureNode -> {
                    writer.write(deploymentElement.toMacroString(stringIdent))
                }
            }
        }
    }

    private fun linkString(link: String?) = if (link != null) """, ${'$'}link="$link"""" else ""

    private fun writeProperties(ele: ModelItem, ident: String, writer: Writer) {
        if (ele.c4Properties == null) {
            return
        }
        val headers = ele.c4Properties!!.header
        if (!headers.isNullOrEmpty()) {
            writer.write("""${ident}SetPropertyHeader(${headers.joinToString(", ") { """"$it"""" }})$separator""")
        } else {
            writer.write("""${ident}WithoutPropertyHeader()$separator""")
        }
        val values = ele.c4Properties?.values ?: listOf()
        values.forEach { row ->
            writer.write("""${ident}AddProperty(${row.joinToString(", ") { value -> """"$value"""" }})$separator""")
        }
    }

    private fun DeploymentNode.toMacroString(ident: String) =
        """${ident}Node($id, "$name", "${technology ?: ""}", "${description ?: ""}", "${
        IconRegistry.iconFileNameFor(
            icon ?: ""
        )
        }"${linkString(link)}){$separator"""

    private fun InfrastructureNode.toMacroString(ident: String) =
        """${ident}Node($id, "$name", "${technology ?: ""}", "${description ?: ""}", "${
        IconRegistry.iconFileNameFor(
            icon ?: ""
        )
        }"${linkString(link)})$separator"""

    private fun SoftwareSystem.toMacroString(id: String, indent: String) =
        """${indent}System${this.type?.c4Type ?: ""}${this.location.toPlantUmlString()}($id, "$name", "${description ?: ""}", "${
        IconRegistry.iconFileNameFor(
            icon ?: ""
        )
        }"${linkString(link)})$separator"""

    private fun Container.toMacroString(id: String, indent: String): String =
        """${indent}Container${this.type?.c4Type ?: ""}${this.location.toPlantUmlString()}($id, "$name", "$technology", "${description ?: ""}", "${
        IconRegistry.iconFileNameFor(
            icon ?: ""
        )
        }"${linkString(link)})$separator"""

    private fun Person.toMacroString(indent: String): String {
        val externalMarker = this.location.toPlantUmlString()
        return """${indent}Person$externalMarker($id, "$name", "${description ?: ""}", "${
        IconRegistry.iconFileNameFor(
            icon ?: ""
        )
        }"${linkString(link)})$separator"""
    }

    private fun Component.toMacroString(indent: String): String {
        return """${indent}Component($id, "$name", "$technology", "${description ?: ""}", "${
        IconRegistry.iconFileNameFor(
            icon ?: ""
        )
        }"${linkString(link)})$separator"""
    }

    override fun writeRelationships(view: View, writer: Writer) {
        val dependencyConfigurations = LayoutRegistry.layoutForKey(view.key).dependencyConfigurations
        dependencyConfigurations.forEach { conf ->
            view.relationships.filter { conf.filter(it.relationship) }.map { relationshipView ->
                relationshipView.apply(conf)
            }
        }
        view.relationships
            .sortedBy { rv: RelationshipView ->
                rv.relationship.source.name + rv.relationship.destination.name
            }
            .forEach { rv: RelationshipView -> writeRelationship(view, rv, writer) }
    }

    private fun RelationshipView.apply(
        conf: DependencyConfiguration
    ): Relationship {
        val rel = this.relationship
        val position = conf.direction
        val mode = conf.mode
        if (position != null) {
            rel.addProperty(C4_LAYOUT_DIRECTION, position.name)
        }
        if (mode != null) {
            rel.addProperty(C4_LAYOUT_MODE, mode.name)
        }
        return rel
    }

    override fun writeRelationship(view: View?, relationshipView: RelationshipView, writer: Writer) {
        writeProperties(relationshipView.relationship, "", writer)

        val relationship = relationshipView.relationship
        var source = relationship.source
        var destination = relationship.destination
        if (relationshipView.isResponse != null && relationshipView.isResponse) {
            source = relationship.destination
            destination = relationship.source
        }
        val mode = if (relationship.properties.containsKey(C4_LAYOUT_MODE)) {
            Mode.valueOf(relationship.properties[C4_LAYOUT_MODE]!!)
        } else {
            Mode.Rel
        }
        var relationshipMacro = mode.macro
        when (mode) {
            Mode.Rel -> {
                var direction = Direction.Down
                if (relationship.properties.containsKey(C4_LAYOUT_DIRECTION)) {
                    direction = Direction.valueOf(relationship.properties[C4_LAYOUT_DIRECTION]!!)
                }
                relationshipMacro = "${relationshipMacro}_${direction.macro()}"
            }
            else -> relationshipMacro = "Rel_$relationshipMacro"
        }
        val description = relationship.description.ifEmpty { " " }
        var relMacro = """$relationshipMacro(${source.id}, ${destination.id}, "$description""""
        if (relationship.technology != null) {
            relMacro += """, "${relationship.technology}""""
        }

        if (!relationship.icon.isNullOrBlank()) {
            relMacro += """, ${'$'}sprite=${IconRegistry.iconFileNameFor(relationship.icon ?: "")}"""
        }
        if (!relationship.link.isNullOrBlank()) {
            relMacro += linkString(relationship.link)
        }
        if (relationship.interactionStyle == InteractionStyle.Asynchronous) {
            relMacro += """, ${'$'}tags="async""""
        }
        writer.write("$relMacro)$separator")
    }

    private fun Location.toPlantUmlString() = if (this == Location.External) "_Ext" else ""

    override fun calculateIndent(indent: Int): String {
        val buf = StringBuilder()
        repeat(indent) {
            buf.append("  ")
        }
        return buf.toString()
    }

    override fun write(view: ContainerView, writer: Writer) {
        writeHeader(view, writer)
        val elements = view.elements.map { it.element }
        val nonContainerElements = elements
            .filter { e -> e !is Container }
            .sortedBy { e: Element -> e.name }
        nonContainerElements.forEach { e: Element -> write(view, e, writer, false) }

        writeContainerForSoftwareSystem(view, writer) { writtenView: ContainerView, usedWriter: Writer ->
            val containersOfSoftwareSystem = elements
                .filterIsInstance<Container>()
                .filter { it.parent == view.softwareSystem }
                .sortedBy { e -> e.name }
            containersOfSoftwareSystem.forEach { e -> write(writtenView, e, usedWriter, true) }
        }

        if (view.externalSoftwareSystemBoundariesVisible) {
            val externalContainerBySystems = elements
                .filterIsInstance<Container>()
                .filter { it.parent is SoftwareSystem }
                .groupBy { it.parent }
                .filter { it.key != view.softwareSystem }
            externalContainerBySystems.forEach {
                writeContainersForSoftwareSystem(it.key as SoftwareSystem, it.value, view, writer)
            }
        } else {
            val externalContainers = elements
                .filterIsInstance<Container>()
                .filter { it.parent != view.softwareSystem }
                .sortedBy { e -> e.name }
            externalContainers.forEach { e: Element -> write(view, e, writer, false) }
        }

        writeRelationships(view, writer)

        writeFooter(view, writer)
    }

    private fun writeContainersForSoftwareSystem(
        system: SoftwareSystem,
        containers: List<Container>,
        view: View,
        writer: Writer
    ) {
        writer.write("System_Boundary(${system.id}_boundary, ${system.name}) {")
        writer.write(separator)
        containers.forEach { write(view, it, writer, true) }
        writer.write("}")
        writer.write(separator)
    }

    override fun write(view: ComponentView, writer: Writer) {
        super.write(view, writer)
        writeFooter(view, writer)
    }

    override fun write(view: DynamicView, writer: Writer) {
        super.write(view, writer)
        writeFooter(view, writer)
    }

    override fun writeSystemLandscapeOrContextView(view: View, writer: Writer, showEnterpriseBoundary: Boolean) {
        super.writeSystemLandscapeOrContextView(view, writer, showEnterpriseBoundary)
        writeFooter(view, writer)
    }

    override fun write(view: DeploymentView, writer: Writer) {
        super.write(view, writer)
        writeFooter(view, writer)
    }

    override fun write(view: View, deploymentNode: DeploymentNode, writer: Writer, indent: Int) {
        if (!view.isElementInView(deploymentNode)) {
            return
        }
        val strIdent = calculateIndent(indent)
        writeProperties(deploymentNode, strIdent, writer)
        writer.write(deploymentNode.toMacroString(strIdent))
        val children: List<DeploymentNode> = deploymentNode
            .children
            .toList()
            .sortedBy { obj: DeploymentNode -> obj.name }
        for (child in children) {
            if (view.isElementInView(child)) {
                write(view, child, writer, indent + 1)
            }
        }
        val infrastructureNodes: List<InfrastructureNode> = deploymentNode
            .infrastructureNodes
            .toList()
            .sortedBy { obj: InfrastructureNode -> obj.name }
        for (infrastructureNode in infrastructureNodes) {
            if (view.isElementInView(infrastructureNode)) {
                write(view, infrastructureNode, writer, indent + 1)
            }
        }
        val softwareSystemInstances: List<SoftwareSystemInstance> = deploymentNode
            .softwareSystemInstances
            .toList()
            .sortedBy { obj: SoftwareSystemInstance -> obj.name }
        for (softwareSystemInstance in softwareSystemInstances) {
            if (view.isElementInView(softwareSystemInstance)) {
                write(view, softwareSystemInstance, writer, indent + 1)
            }
        }
        val containerInstances: List<ContainerInstance> = deploymentNode
            .containerInstances
            .toList()
            .sortedBy { obj: ContainerInstance -> obj.name }
        for (containerInstance in containerInstances) {
            if (view.isElementInView(containerInstance)) {
                write(view, containerInstance, writer, indent + 1)
            }
        }
        writer.write("$strIdent}")
        writer.write(separator)
    }

    private fun writeFooter(view: View, writer: Writer) {
        val layout = LayoutRegistry.layoutForKey(view.key)
        writeLayout(writer, layout)
        writer.write("@enduml")
    }

    private fun writeLayout(
        writer: Writer,
        layout: C4PlantUmlLayout
    ) {
        writer.write(separator)
        if (layout.lineType != null) {
            writer.write(layout.lineType.macro + separator)
        }
        if (layout.nodeSep != null) {
            writer.write("skinparam nodesep ${layout.nodeSep}" + separator)
        }
        if (layout.rankSep != null) {
            writer.write("skinparam ranksep ${layout.rankSep}" + separator)
        }
        if (layout.legend != Legend.None) {
            writer.write(layout.legend.toMacro(layout.hideStereotypes) + separator + separator)
        }
    }

    private fun Legend.toMacro(hideStereotypes: Boolean): String {
        val macro = this.macro
        return when (this) {
            Legend.ShowStaticLegend -> "$macro()"
            Legend.ShowLegend -> "$macro($hideStereotypes)"
            Legend.ShowFloatingLegend -> "$macro(LEGEND, $hideStereotypes)"
            else -> ""
        }
    }

    @Suppress("EmptyFunctionBlock")
    override fun writeFooter(writer: Writer) {
    }

    private fun writeAsyncRelTag(writer: Writer) {
        writer.write(
            """AddRelTag("async", ${'$'}lineStyle = DashedLine())"""
        )
        writer.write(separator + separator)
    }
}
