package com.example.searchsample.search.repository;

import com.example.searchsample.search.entity.SearchWordHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchWordHistoryRepository extends JpaRepository<SearchWordHistory, Long>, SearchWordHistoryRepositoryCustom {
}
