package com.github.chriskn.structurizrextension.internal.export.writer

internal fun linkString(link: String?) = if (link != null) """, ${'$'}link="$link"""" else ""
