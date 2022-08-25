package com.example.searchsample.search.constant;

/**
 * 카카오 개발자 관련 Keys.
 */
public class KakaoKeys {

    protected KakaoKeys() {

    }

    /**
     * 키워드로 장소 검색 JSON
     */
    public static final String SEARCH_PLACE_JSON_URI = "/v2/local/search/keyword.json";
    /**
     * 키워드로 장소 검색 XML
     */
    public static final String SEARCH_PLACE_XML_URI = "/v2/local/search/keyword.xml";

    /**
     * 검색을 원하는 질의어
     */
    public static final String SEARCH_WORD = "query";
    /**
     * 한 페이지에 보여질 문서의 개수, 1~15 사이, 기본 값 15
     */
    public static final String RESULT_SIZE = "size";
    /**
     * 결과 페이지 번호, 1-45 사이, 기본 값 1
     */
    public static final String RESULT_PAGE_SIZE = "page";
    /**
     * 결과 정렬 순서, distance 정렬을 원할 때는 기준 좌표로 쓰일 x, y와 함께 사용, distance 또는 accuracy, 기본 accuracy
     */
    public static final String RESULT_SORT = "sort";


}
