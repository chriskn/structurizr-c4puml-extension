package com.github.chriskn.structurizrextension.internal.export.writer.relationship

import com.structurizr.view.RelationshipView

internal class DynamicViewRelationshipViewComparator : Comparator<RelationshipView> {

    @Suppress("ReturnCount")
    override fun compare(self: RelationshipView, other: RelationshipView): Int {
        val selfParts = self.order.split(".")
        val otherParts = other.order.split(".")

        selfParts.forEachIndexed { index, selfPart ->
            val selfPartToInt = selfPart.toInt()
            val otherPart = otherParts.getOrNull(index)

            when {
                // 5.1.1 > 5.1
                otherPart == null -> return 1
                selfPartToInt > otherPart.toInt() -> return 1
                selfPartToInt < otherPart.toInt() -> return -1
            }
        }

        return 0
    }
}
