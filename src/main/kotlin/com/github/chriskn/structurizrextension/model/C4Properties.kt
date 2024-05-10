package com.github.chriskn.structurizrextension.model

private const val MAX_ROW_SIZE = 4

/**
 * Properties can be defined for Elements and Dependencies.
 *
 * They will be rendered as table and can be used to document detailed concepts like deployments.
 * By default, no (table) header is added (WithoutPropertyHeader() in C4-PlantUML).
 * The header must have at least the number of columns of the longest row.
 * Number rows should not exceed [MAX_ROW_SIZE] or number of header rows
 * C4PlantUml does not support single row properties.
 */
data class C4Properties(
    /**
     * Header of the rendered property table. Empty be default
     */
    val header: List<String>? = null,
    /**
     * Values of the property table as list of rows containing a list of columns.
     */
    val values: List<List<String>>
) {

    init {
        require(values.all { it.size <= MAX_ROW_SIZE && (header == null || it.size <= header.size) }) {
            "Number of rows should not exceed $MAX_ROW_SIZE or number of header rows"
        }
        // Not supported. Issue: https://github.com/plantuml-stdlib/C4-PlantUML/issues/355
        require(values.all { it.size > 1 } && (header == null || header.size > 1)) {
            "C4PlantUml does not support single row properties or property header"
        }
    }
}
