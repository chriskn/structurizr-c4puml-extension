package com.github.chriskn.structurizrextension.internal.export.writer

import com.github.chriskn.structurizrextension.api.model.c4Properties
import com.structurizr.export.IndentingWriter
import com.structurizr.model.ModelItem

internal object PropertyWriter {

    fun writeProperties(item: ModelItem, writer: IndentingWriter) {
        val c4Properties = item.c4Properties ?: return
        val headers = c4Properties.header
        if (!headers.isNullOrEmpty()) {
            writer.writeLine("""SetPropertyHeader(${headers.joinToString(", ") { """"$it"""" }})""")
        } else {
            writer.writeLine("""WithoutPropertyHeader()""")
        }
        val values = c4Properties.values
        values.forEach { row ->
            writer.writeLine("""AddProperty(${row.joinToString(", ") { value -> """"$value"""" }})""")
        }
    }
}
