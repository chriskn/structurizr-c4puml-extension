package com.github.chriskn.structurizrextension.export.writer

import com.github.chriskn.structurizrextension.model.c4Properties
import com.structurizr.export.IndentingWriter
import com.structurizr.model.ModelItem

object PropertyWriter {
    internal fun writeProperties(item: ModelItem, writer: IndentingWriter) {
        if (item.c4Properties == null) {
            return
        }
        val headers = item.c4Properties!!.header
        if (!headers.isNullOrEmpty()) {
            writer.writeLine("""SetPropertyHeader(${headers.joinToString(", ") { """"$it"""" }})""")
        } else {
            writer.writeLine("""WithoutPropertyHeader()""")
        }
        val values = item.c4Properties?.values ?: listOf()
        values.forEach { row ->
            writer.writeLine("""AddProperty(${row.joinToString(", ") { value -> """"$value"""" }})""")
        }
    }
}
