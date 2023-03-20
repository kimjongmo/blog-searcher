package com.example.blogsearcher.infra.persistence.dbconverter

import com.example.blogsearcher.domain.search.vo.Keyword
import javax.persistence.AttributeConverter

class KeywordDbConverter : AttributeConverter<Keyword, String> {
    override fun convertToDatabaseColumn(attribute: Keyword?): String? {
        return attribute?.value
    }

    override fun convertToEntityAttribute(dbData: String?): Keyword? {
        return dbData?.let { Keyword(dbData) }
    }
}
