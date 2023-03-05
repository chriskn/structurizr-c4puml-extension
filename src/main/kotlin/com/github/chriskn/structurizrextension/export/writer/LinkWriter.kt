package com.github.chriskn.structurizrextension.export.writer

fun linkString(link: String?) = if (link != null) """, ${'$'}link="$link"""" else ""
