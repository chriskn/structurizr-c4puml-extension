package com.github.chriskn.structurizrextension.api.view.layout

object LayoutRegistry {

    private val keyToLayout = mutableMapOf<String, C4PlantUmlLayout>()

    fun registerLayoutForKey(key: String, layout: C4PlantUmlLayout) = keyToLayout.put(key, layout)

    fun layoutForKey(key: String): C4PlantUmlLayout = keyToLayout.getOrDefault(key, C4PlantUmlLayout())
}
