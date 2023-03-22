## 결과물
`jar` 디렉토리안에 `blog-searcher.jar` 파일을 다운로드받으면 됩니다.

## API

애플리케이션 실행 후 `http://localhost:8888/swagger-ui/index.html` 경로로 들어가면 API 스펙에 대하여 상세한 내용을 볼 수 있습니다.

### GET /api/v1/blog/search

| 파라미터 | 타입   | 설명             | 제약                           |
|------|------|----------------|------------------------------|
|keyword|String| 검색할 키워드        | 빈 값 허용 안함                    |
|page|Int| 검색할 페이지        | 1 ~ 50                       |
|size|Int| 페이지에 노출될 글 사이즈 | 1 ~ 50                       |
|sort|String| 정렬 옵션          | ACCURACY(정확도순), RECENCY(최신순) |

요청 예시

```text
http://localhost:8888/api/v1/blog/search?keyword=테스트&page=2&size=20&sort=RECENCY
```

### GET /api/rank/keyword-from-db

| 파라미터 | 타입   | 설명        | 제약                              |
|------|------|-----------|---------------------------------|
|from|String| 검색 시작 범위  | 필수 입력. 패턴 'yyyy-MM-dd HH:mm:ss' |
|to|String| 검색 마지막 범위 | 필수 입력. 패턴 'yyyy-MM-dd HH:mm:ss'        |

요청 예시

```text
http://localhost:8888/api/rank/keyword-from-db?from=2023-03-22 01:07:21&to=2023-03-22 08:30:00
```

### GET /api/rank/keyword-from-cache

| 파라미터 | 타입  | 설명        | 제약    |
|------|-----|-----------|-------|
| rank | Int | 조회할 랭킹 순위 | 필수 입력 |

요청 예시

```text
http://localhost:8888/api/rank/keyword-from-cache?rank=10
```

## 사용 라이브러리 목록

|라이브러리| 목적                                                       |
|:---:|:---------------------------------------------------------|
|io.github.microutils:kotlin-logging-jvm:3.0.4| 코틀린 로깅 프레임워크|
|com.fasterxml.jackson.module:jackson-module-kotlin| jackson에서 코틀린 직렬화 및 역직렬화 서포트|
|org.mockito.kotlin:mockito-kotlin:4.1.0| 테스트 작성시 코틀린 mocking|
|com.github.ben-manes.caffeine:caffeine| 애플리케이션 메모리를 이용하여 캐시를 사용하기 위함|
|io.github.resilience4j:resilience4j-spring-boot2| 외부 API 장애 관리를 위해 사용|
|com.squareup.retrofit2:retrofit| HTTP 클라이언트 라이브러리, 비동기 호출 가능|
|com.squareup.retrofit2:converter-jackson| Retrofit에서 Jackson JSON 라이브러리를 사용하여 JSON 데이터를 직렬화 및 역직렬화 |
|com.squareup.okhttp3:logging-interceptor| OkHttp 클라이언트에 로깅 인터셉터|
|com.fasterxml.jackson.datatype:jackson-datatype-jsr310| jackson 날짜 및 시간 관련 타입 서포트|
|com.squareup.okhttp3:mockwebserver| 테스트시 OkHttp mock 서버를 만들기 위해 사용|
|com.querydsl:querydsl-jpa| 가독성 및 타입 세이프한 쿼리 작성|
|org.springdoc:springdoc-openapi-ui| open api 문서 자동 생성|