package com.structurizr.model

fun Model.enterprise(name: String) {
    @Suppress("DEPRECATION")
    this.enterprise = Enterprise(name)
}

@Suppress("DEPRECATION")
fun Model.enterprise(): Enterprise = this.enterprise
