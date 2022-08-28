package com.github.chriskn.structurizrextension.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class C4PropertiesTest {

    @Test
    fun `throws exception if num rows greater max rows`() {
        assertThrows<IllegalArgumentException> {
            C4Properties(
                values = listOf(
                    listOf("", "", "", "", "")
                )
            )
        }
    }

    @Test
    fun `should not throw exception if num rows equal max rows`() {
        C4Properties(
            values = listOf(
                listOf("", "", "", "")
            )
        )
    }

    @Test
    fun `should throw exception if num rows greater header rows`() {
        assertThrows<IllegalArgumentException> {
            C4Properties(
                header = listOf("", ""),
                values = listOf(
                    listOf("", ""),
                    listOf("", "", "")
                )
            )
        }
    }

    @Test
    fun `should not throw exception if num rows equal or smaller header rows`() {
        C4Properties(
            header = listOf("", ""),
            values = listOf(
                listOf("", ""),
                listOf("")
            )
        )
    }
}
