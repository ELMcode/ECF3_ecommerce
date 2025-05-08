package com.ut.ecommerce.searchsuggestionservice.service;

import com.ut.ecommerce.searchsuggestionservice.entity.SearchSuggestion;
import com.ut.ecommerce.searchsuggestionservice.dao.SearchSuggestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchSuggestionService {

    private final SearchSuggestionRepository searchSuggestionRepository;

    @Autowired
    public SearchSuggestionService(SearchSuggestionRepository searchSuggestionRepository) {
        this.searchSuggestionRepository = searchSuggestionRepository;
    }

    public List<String> getDefaultSuggestions() {
        List<SearchSuggestion> suggestions = searchSuggestionRepository.findDefaultSuggestions();
        return mapToKeywords(suggestions);
    }

    public List<String> getSuggestionsByPrefix(String prefix) {
        List<SearchSuggestion> suggestions = searchSuggestionRepository.findSuggestionsByPrefix(prefix);
        return mapToKeywords(suggestions);
    }

    private List<String> mapToKeywords(List<SearchSuggestion> suggestions) {
        return suggestions.stream()
                .map(SearchSuggestion::getKeyword)
                .collect(Collectors.toList());
    }
}
