package com.ut.ecommerce.searchsuggestionservice.dao;

import com.ut.ecommerce.searchsuggestionservice.entity.SearchSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchSuggestionRepository extends JpaRepository<SearchSuggestion, Long> {
    
    @Query("SELECT s FROM SearchSuggestion s WHERE s.isDefault = true ORDER BY s.searchCount DESC")
    List<SearchSuggestion> findDefaultSuggestions();
    
    @Query("SELECT s FROM SearchSuggestion s WHERE LOWER(s.keyword) LIKE LOWER(CONCAT(:prefix, '%')) ORDER BY s.searchCount DESC")
    List<SearchSuggestion> findSuggestionsByPrefix(@Param("prefix") String prefix);
}
