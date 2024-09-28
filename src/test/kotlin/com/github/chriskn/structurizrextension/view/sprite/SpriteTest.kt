// package com.github.chriskn.structurizrextension.view.sprite
//
// import com.github.chriskn.structurizrextension.api.view.style.createElementStyle
// import com.github.chriskn.structurizrextension.api.view.style.sprite
// import org.assertj.core.api.Assertions.assertThat
// import org.junit.jupiter.api.Test
// import org.junit.jupiter.api.assertThrows
//
// class SpriteTest {
//
//    @Test
//    fun `sprite is serialized and deserialized correctly when URI with scale is used`() {
//        val expectedSprite = SpriteOld(URI.create("img:https://plantuml.com/logo.png"), 0.4)
//        val style = createElementStyle("test", sprite = expectedSprite)
//
//        assertThat(style.sprite).isEqualTo(expectedSprite)
//    }
//
//    @Test
//    fun `sprite is serialized and deserialized correctly when URI without scale is used`() {
//        val expectedSprite = SpriteOld(URI.create("img:https://plantuml.com/logo.png"))
//        val style = createElementStyle("test", sprite = expectedSprite)
//
//        assertThat(style.sprite).isEqualTo(expectedSprite)
//    }
//
//    @Test
//    fun `sprite is serialized and deserialized correctly when openIconicIcon with scale is used`() {
//        val expectedSprite = SpriteOld("folder", scale = 0.4)
//        val style = createElementStyle("test", sprite = expectedSprite)
//
//        assertThat(style.sprite).isEqualTo(expectedSprite)
//    }
//
//    @Test
//    fun `sprite is serialized and deserialized correctly when openIconicIcon with color is used`() {
//        val expectedSprite = SpriteOld("folder", color = "grey")
//        val style = createElementStyle("test", sprite = expectedSprite)
//
//        assertThat(style.sprite).isEqualTo(expectedSprite)
//    }
//
//    @Test
//    fun `sprite is serialized and deserialized correctly when openIconicIcon with scale and color is used`() {
//        val expectedSprite = SpriteOld("folder", color = "grey", scale = 0.1)
//        val style = createElementStyle("test", sprite = expectedSprite)
//
//        assertThat(style.sprite).isEqualTo(expectedSprite)
//    }
//
//    @Test
//    fun `sprite is serialized and deserialized correctly when openIconicIcon without scale and color is used`() {
//        val expectedSprite = SpriteOld("folder")
//        val style = createElementStyle("test", sprite = expectedSprite)
//
//        assertThat(style.sprite).isEqualTo(expectedSprite)
//    }
//
//    @Test
//    fun `IllegalArgumentException is thrown when uri without image schema is used`() {
//        assertThrows<IllegalArgumentException> {
//            SpriteOld(URI.create("https://plantuml.com/logo.png"))
//        }
//    }
//
//    @Test
//    fun `IllegalArgumentException is thrown when uri with different schema is used`() {
//        assertThrows<IllegalArgumentException> {
//            SpriteOld(URI.create("file:https://plantuml.com/logo.png"))
//        }
//    }
//
//    @Test
//    fun `IllegalArgumentException is thrown when uri is not absolute`() {
//        assertThrows<IllegalArgumentException> {
//            SpriteOld(URI.create("img:https://plantuml.com/"))
//        }
//    }
//
//    @Test
//    fun `IllegalArgumentException is thrown if scale is negative `() {
//        assertThrows<IllegalArgumentException> {
//            SpriteOld(URI.create("img:https://plantuml.com//logo.png"), -0.1)
//        }
//    }
//
//    @Test
//    fun `IllegalArgumentException is thrown if scale is zero `() {
//        assertThrows<IllegalArgumentException> {
//            SpriteOld(URI.create("img:https://plantuml.com//logo.png"), 0.0)
//        }
//    }
//
//    @Test
//    fun `sprite is serialized and deserialized correctly when openIconicIcon is used`() {
//        val expectedSprite = SpriteOld("iconName", 0.4)
//        val style = createElementStyle("test", sprite = expectedSprite)
//
//        assertThat(style.sprite).isEqualTo(expectedSprite)
//    }
//
//    @Test
//    fun `IllegalArgumentException is thrown for invalid sprite color color`() {
//        assertThrows<IllegalArgumentException> {
//            SpriteOld("folder", color = "123")
//        }
//    }
// }
