package com.example.blogsearcher.infra.persistence.dbconverter

import com.example.blogsearcher.domain.search.vo.Keyword
import javax.persistence.AttributeConverter

/**
 * 객체를 DB에 저장 및 조회할 때 사용하기 위한 컨버터
 * */
class KeywordDbConverter : AttributeConverter<Keyword, String> {
    override fun convertToDatabaseColumn(attribute: Keyword?): String? {
        return attribute?.value
    }

    override fun convertToEntityAttribute(dbData: String?): Keyword? {
        return dbData?.let { Keyword(dbData) }
    }
}
