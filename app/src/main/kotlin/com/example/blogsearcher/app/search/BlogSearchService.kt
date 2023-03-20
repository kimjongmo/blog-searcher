package com.example.blogsearcher.app.search

import com.example.blogsearcher.app.common.EventPublisher
import com.example.blogsearcher.domain.search.BlogSearchResult
import com.example.blogsearcher.domain.search.BlogSearchSourceRepository
import com.example.blogsearcher.domain.search.event.BlogSearchEvent
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.LocalDateTime

private val log = KotlinLogging.logger { }

interface BlogSearchService {
    fun search(keyword: Keyword, page: Page): BlogSearchResult?
}

@Service
class BlogSearchServiceImpl(
    private val blogSearchSourceRepository: BlogSearchSourceRepository,
    private val eventPublisher: EventPublisher
) : BlogSearchService {
    /**
     * @param keyword 검색 키워드
     * @param page 검색 페이지 정보
     * @throws NotFoundSearchSourceException 검색 소스를 가져오지 못한 경우 발생
     * */
    override fun search(keyword: Keyword, page: Page): BlogSearchResult? {
        val sources = blogSearchSourceRepository.findAll()

        if (sources.isEmpty()) {
            throw NotFoundSearchSourceException()
        }

        val selectedSource = sources.first()

        return runCatching {
            selectedSource.query(keyword, page)
        }.onSuccess {
            eventPublisher.publish(BlogSearchEvent(selectedSource.searchVendor, keyword, LocalDateTime.now()))
        }.onFailure {
            log.error("검색에 실패하였습니다. vendor = ${selectedSource.searchVendor}, message = ${it.message}")
        }.getOrNull()
    }
}
