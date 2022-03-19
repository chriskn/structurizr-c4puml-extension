package com.github.chriskn.structurizrextension.model

enum class C4Type(val c4Type: String) {
    QUEUE("Queue"),
    DATABASE("Db");

    companion object {
        fun fromC4Type(c4Type: String): C4Type {
            return values().first { it.c4Type == c4Type }
        }
    }
}
