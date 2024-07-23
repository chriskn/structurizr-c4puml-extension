package com.github.chriskn.structurizrextension.internal.export.writer.relationship

import com.structurizr.view.RelationshipView

internal class RelationshipViewComparator : Comparator<RelationshipView> {

    override fun compare(self: RelationshipView, other: RelationshipView): Int {
        val selfSourceTarget = self.relationship.source.name + self.relationship.destination.name
        val otherSourceTarget = other.relationship.source.name + other.relationship.destination.name

        return when {
            selfSourceTarget > otherSourceTarget -> 1
            selfSourceTarget < otherSourceTarget -> -1
            else -> 0
        }
    }
}
