package com.example.blogsearcher.infra.retrofit.kakao.factory

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class LocalDateTimeWithZoneSerializer : JsonSerializer<LocalDateTime>() {
    override fun serialize(value: LocalDateTime, gen: JsonGenerator, serializers: SerializerProvider?) {
        val zonedDateTime = value.atZone(ZoneId.systemDefault())
        val formattedDateTime = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))
        gen.writeString(formattedDateTime)
    }
}
