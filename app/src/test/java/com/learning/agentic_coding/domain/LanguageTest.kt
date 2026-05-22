package com.learning.agentic_coding.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LanguageTest {

    @Test
    fun `fromCode resolves supported codes case-insensitively`() {
        assertEquals(Language.VN, Language.fromCode("vn"))
        assertEquals(Language.EN, Language.fromCode("EN"))
        assertEquals(Language.JA, Language.fromCode("Ja"))
    }

    @Test
    fun `fromCode returns null for unsupported codes`() {
        assertNull(Language.fromCode(null))
        assertNull(Language.fromCode(""))
        assertNull(Language.fromCode("FR"))
        assertNull(Language.fromCode("zz"))
    }

    @Test
    fun `default language is VN per design spec`() {
        assertEquals(Language.VN, Language.DEFAULT)
    }

    @Test
    fun `locale tags match BCP-47 codes`() {
        assertEquals("vi", Language.VN.toLocale().language)
        assertEquals("en", Language.EN.toLocale().language)
        assertEquals("ja", Language.JA.toLocale().language)
    }
}
