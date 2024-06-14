package com.github.chriskn.structurizrextension.internal.export.writer

import com.github.chriskn.structurizrextension.api.view.layout.C4PlantUmlLayout
import com.github.chriskn.structurizrextension.api.view.layout.LayoutRegistry
import com.github.chriskn.structurizrextension.api.view.layout.Legend
import com.structurizr.export.IndentingWriter
import com.structurizr.view.ModelView

internal object FooterWriter {

    fun writeFooter(view: ModelView, writer: IndentingWriter) {
        val layout = LayoutRegistry.layoutForKey(view.key)
        if (layout.hasPostamble()) {
            writer.writeLine()
            writeLayout(layout, writer)
            writer.writeLine()
        }
        writer.writeLine("@enduml")
    }

    private fun writeLayout(
        layout: C4PlantUmlLayout,
        writer: IndentingWriter
    ) {
        if (layout.lineType != null) {
            writer.writeLine(layout.lineType.macro)
        }
        if (layout.nodeSep != null) {
            writer.writeLine("skinparam nodesep ${layout.nodeSep}")
        }
        if (layout.rankSep != null) {
            writer.writeLine("skinparam ranksep ${layout.rankSep}")
        }
        val macro = layout.legend.toMacro(layout.hideStereotypes)
        if (macro.isNotEmpty()) {
            writer.writeLine(macro)
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
}
