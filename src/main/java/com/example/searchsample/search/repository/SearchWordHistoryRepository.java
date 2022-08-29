package com.example.searchsample.search.repository;

import com.example.searchsample.search.entity.SearchWordHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Search word history repository.
 */
public interface SearchWordHistoryRepository extends JpaRepository<SearchWordHistory, Long>, SearchWordHistoryRepositoryCustom {
}
