package com.example.blogsearcher.infra.retrofit.kakao.factory

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeWithZoneDeserializer : JsonDeserializer<LocalDateTime>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDateTime {
        val zonedDateTime = ZonedDateTime.parse(p.text, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))
        return zonedDateTime.toLocalDateTime()
    }
}
