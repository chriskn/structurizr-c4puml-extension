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
import com.github.chriskn.structurizrextension.internal.export.view.style.propertyName
import com.github.chriskn.structurizrextension.internal.export.view.style.toJson
import com.structurizr.view.View

/**
 * Add element style to this view
 *
 * @param elementStyle the element style to add
 */
fun View.addElementStyle(elementStyle: ElementStyle) {
    this.addProperty(elementStyle.propertyName(), elementStyle.toJson())
}

/**
 * Get element styles
 *
 * @return all element style for this view
 */
fun View.getElementStyles(): List<ElementStyle> =
    this.properties
        .filter { it.key.startsWith(ELEMENT_STYLE_PROPERTY_NAME_PREFIX) }
        .map { elementStyleFromJson(it.value) }

/**
 * Add boundary style to this view
 *
 * @param boundaryStyle the boundary style to add
 */
fun View.addBoundaryStyle(boundaryStyle: BoundaryStyle) {
    this.addProperty(boundaryStyle.propertyName(), boundaryStyle.toJson())
}

/**
 * Get boundary styles
 *
 * @return all boundary styles for this view
 */
fun View.getBoundaryStyles(): List<BoundaryStyle> =
    this.properties
        .filter { it.key.startsWith(BOUNDARY_STYLE_PROPERTY_NAME_PREFIX) }
        .map { boundaryStyleFromJson(it.value) }

/**
 * Add person style to this view
 *
 * @param personStyle the person style to add
 */
fun View.addPersonStyle(personStyle: PersonStyle) {
    this.addProperty(personStyle.propertyName(), personStyle.toJson())
}

/**
 * Get person styles
 *
 * @return all person styles for this view
 */
fun View.getPersonStyles(): List<PersonStyle> =
    this.properties
        .filter { it.key.startsWith(PERSON_STYLE_PROPERTY_NAME_PREFIX) }
        .map { personStyleFromJson(it.value) }

/**
 * Add dependency style to this view
 *
 * @param dependencyStyle the dependency style to add
 */
fun View.addDependencyStyle(dependencyStyle: DependencyStyle) {
    this.addProperty(dependencyStyle.propertyName(), dependencyStyle.toJson())
}

/**
 * Get dependency styles
 *
 * @return all dependency styles for this view
 */
fun View.getDependencyStyles(): List<DependencyStyle> =
    this.properties
        .filter { it.key.startsWith(DEPENDENCY_STYLE_PROPERTY_NAME_PREFIX) }
        .map { dependencyStyleFromJson(it.value) }
