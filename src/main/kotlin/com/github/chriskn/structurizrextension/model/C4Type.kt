package com.github.chriskn.structurizrextension.model

/**
 * Determines the shape of an Element in the rendered diagrams.
 */
enum class C4Type(val c4Type: String) {
    /**
     * The QUEUE type can be used to visualize e.g. message queues
     */
    QUEUE("Queue"),

    /**
     * The DATABASE type can be used to visualize databases
     */
    DATABASE("Db");

    companion object {
        fun fromC4Type(c4Type: String): C4Type {
            return values().first { it.c4Type == c4Type }
        }
    }
}
