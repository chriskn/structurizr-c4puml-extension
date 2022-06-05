package com.github.chriskn.structurizrextension.model

private const val MAX_ROW_SIZE = 4

/**
 * Properties can be defined for Elements and Dependencies.
 *
 * They will be rendered as table and can be used to document detailed concepts like deployments.
 * By default, no (table) header is added (WithoutPropertyHeader() in C4-PlantUML).
 * The header must have at least the number of columns of the longest row.
 * Max row size is [MAX_ROW_SIZE]
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
            "Number of values per column should no exceed $MAX_ROW_SIZE and number of header rows"
        }
    }
}
