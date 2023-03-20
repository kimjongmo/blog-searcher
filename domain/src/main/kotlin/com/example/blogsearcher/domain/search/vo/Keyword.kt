package com.example.blogsearcher.domain.search.vo

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import java.io.IOException

data class Keyword(val value: String)

class KeywordJsonSerializer : com.fasterxml.jackson.databind.JsonSerializer<Keyword>() {
    @Throws(IOException::class)
    override fun serialize(src: Keyword, g: JsonGenerator, sp: SerializerProvider) {
        g.writeString(src.value)
    }
}

class KeywordJsonDeserializer : com.fasterxml.jackson.databind.JsonDeserializer<Keyword>() {
    @Throws(IOException::class)
    override fun deserialize(jp: JsonParser, ctx: DeserializationContext): Keyword {
        return Keyword(jp.text)
    }
}
