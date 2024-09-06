package com.github.chriskn.structurizrextension.api.view.style

import com.github.chriskn.structurizrextension.api.view.style.styles.BoundaryStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.DependencyStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.ElementStyle
import com.github.chriskn.structurizrextension.api.view.style.styles.PersonStyle
import com.github.chriskn.structurizrextension.internal.export.view.style.BOUNDARY_STYLE_PROPERTY_NAME_PREFIX
import com.github.chriskn.structurizrextension.internal.export.view.style.DEPENDENCY_STYLE_PROPERTY_NAME_PREFIX
import com.github.chriskn.structurizrextension.internal.export.view.style.ELEMENT_STYLE_PROPERTY_NAME_PREFIX
import com.github.chriskn.structurizrextension.internal.export.view.style.PERSON_STYLE_PROPERTY_NAME_PREFIX
import com.github.chriskn.structurizrextension.internal.export.view.style.boundaryStyleFromJson
import com.github.chriskn.structurizrextension.internal.export.view.style.dependencyStyleFromJson
import com.github.chriskn.structurizrextension.internal.export.view.style.elementStyleFromJson
import com.github.chriskn.structurizrextension.internal.export.view.style.personStyleFromJson
import com.github.chriskn.structurizrextension.internal.export.view.style.toJson
import com.structurizr.view.ViewSet

/**
 * Add element style to all views
 *
 * @param elementStyle the element style to add
 */
fun ViewSet.addElementStyle(elementStyle: ElementStyle) {
    this.configuration.addProperty("$ELEMENT_STYLE_PROPERTY_NAME_PREFIX:${elementStyle.tag}", elementStyle.toJson())
}

/**
 * Get element styles
 *
 * @return the list of element styles assigned to the ViewSet
 */
fun ViewSet.getElementStyles(): List<ElementStyle> =
    this.configuration.properties
        .filter { it.key.startsWith(ELEMENT_STYLE_PROPERTY_NAME_PREFIX) }
        .map { elementStyleFromJson(it.value) }

/**
 * Add boundary style to all views
 *
 * @param boundaryStyle the boundary style to add
 */
fun ViewSet.addBoundaryStyle(boundaryStyle: BoundaryStyle) {
    this.configuration.addProperty("$BOUNDARY_STYLE_PROPERTY_NAME_PREFIX:${boundaryStyle.tag}", boundaryStyle.toJson())
}

/**
 * Get boundary styles
 *
 * @return the list of boundary styles assigned to the ViewSet
 */
fun ViewSet.getBoundaryStyles(): List<BoundaryStyle> =
    this.configuration.properties
        .filter { it.key.startsWith(BOUNDARY_STYLE_PROPERTY_NAME_PREFIX) }
        .map { boundaryStyleFromJson(it.value) }

/**
 * Add person style to all views
 *
 * @param personStyle the person style to add
 */
fun ViewSet.addPersonStyle(personStyle: PersonStyle) {
    this.configuration.addProperty("$PERSON_STYLE_PROPERTY_NAME_PREFIX:${personStyle.tag}", personStyle.toJson())
}

/**
 * Get person styles
 *
 * @return the list of person styles assigned to the ViewSet
 */
fun ViewSet.getPersonStyles(): List<PersonStyle> =
    this.configuration.properties
        .filter { it.key.startsWith(PERSON_STYLE_PROPERTY_NAME_PREFIX) }
        .map { personStyleFromJson(it.value) }

/**
 * Add dependency style to all views
 *
 * @param dependencyStyle the dependency style to add
 */
fun ViewSet.addDependencyStyle(dependencyStyle: DependencyStyle) {
    this.configuration.addProperty(
        "$DEPENDENCY_STYLE_PROPERTY_NAME_PREFIX:${dependencyStyle.tag}",
        dependencyStyle.toJson()
    )
}

/**
 * Get dependency styles
 *
 * @return the list of dependency styles assigned to the ViewSet
 */
fun ViewSet.getDependencyStyles(): List<DependencyStyle> =
    this.configuration.properties
        .filter { it.key.startsWith(DEPENDENCY_STYLE_PROPERTY_NAME_PREFIX) }
        .map { dependencyStyleFromJson(it.value) }
