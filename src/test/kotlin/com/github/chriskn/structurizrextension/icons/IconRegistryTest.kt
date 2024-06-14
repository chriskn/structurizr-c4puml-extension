package com.github.chriskn.structurizrextension.icons

import com.github.chriskn.structurizrextension.internal.icons.IconRegistry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class IconRegistryTest {

    @Test
    fun `icon exist after it was added`() {
        IconRegistry.addIcon("test", "https://someUrl/newIcon.puml")

        assertThat(IconRegistry.exists("test")).isTrue
    }

    @Test
    fun `icon can be overridden`() {
        val expectedUrl = "https://someUrl/kotlin2.puml"
        IconRegistry.exists("kotlin")
        IconRegistry.addIcon("kotlin", expectedUrl)

        assertThat(IconRegistry.iconUrlFor("kotlin")).isEqualTo(expectedUrl)
    }

    @Test
    fun `throws MalformedURLException if invalid url is added`() {
        assertThrows<IllegalArgumentException> {
            IconRegistry.addIcon("kotlin", "invalidURl.puml")
        }
    }

    @Test
    fun `throws IllegalArgumentException if url does not point to puml file`() {
        assertThrows<IllegalArgumentException> {
            IconRegistry.addIcon("kotlin", "invalidURl")
        }
    }

    @Test
    fun `iconFileNameFor returns null if name is null`() {
        assertThat(IconRegistry.iconFileNameFor(null)).isNull()
    }

    @Test
    fun `iconFileNameFor return null if no icon file exists for name`() {
        assertThat(IconRegistry.iconFileNameFor("iDoNotExist")).isNull()
    }

    @AfterEach
    fun resetIconRegistry() {
        IconRegistry.reset()
    }
}
