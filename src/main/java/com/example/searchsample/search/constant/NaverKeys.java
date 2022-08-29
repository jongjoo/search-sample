package com.example.searchsample.search.constant;

/**
 * 네이버 개발자 관련 Keys.
 */
public class NaverKeys {

    /**
     * Instantiates a new Naver keys.
     */
    protected NaverKeys() {

    }

    /**
     * HeaderName Client ID
     */
    public static final String NAVER_CLIENT_ID = "X-Naver-Client-Id";
    /**
     * HeaderName Client Secret
     */
    public static final String NAVER_CLIENT_SECRET = "X-Naver-Client-Secret";

    /**
     * 네이버 지역 서비스에 등록된 각 지역별 업체 및 상호 검색 결과를 출력해주는 REST API입니다. JSON
     */
    public static final String SEARCH_PLACE_JSON_URI = "/v1/search/local.json";
    /**
     * 네이버 지역 서비스에 등록된 각 지역별 업체 및 상호 검색 결과를 출력해주는 REST API입니다. XML
     */
    public static final String SEARCH_PLACE_XML_URI = "/v1/search/local.xml";
    /**
     * 검색을 원하는 문자열로서 UTF-8로 인코딩한다.
     */
    public static final String SEARCH_WORD = "query";
    /**
     * 검색 결과 출력 건수 지정.
     */
    public static final String RESULT_SIZE = "display";
    /**
     * 검색 시작 위치로 1만 가능.
     */
    public static final String RESULT_PAGE_SIZE = "start";
    /**
     * 정렬 옵션: random(유사도순), comment(카페/블로그 리뷰 개수 순).
     */
    public static final String RESULT_SORT = "sort";


}
