package com.github.chriskn.structurizrextension.model

private const val MAX_ROW_SIZE = 4

data class C4Properties(
    val headers: List<String>? = null,
    val values: List<List<String>>
) {

    init {
        require(values.all { it.size <= MAX_ROW_SIZE && (headers == null || it.size <= headers.size) }) {
            "Number of values per column should no exceed $MAX_ROW_SIZE and number of header rows"
        }
    }
}
