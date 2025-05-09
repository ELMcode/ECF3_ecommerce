package com.ut.ecommerce.searchsuggestionservice.controller;

import com.ut.ecommerce.searchsuggestionservice.model.SearchSuggestionItem;
import com.ut.ecommerce.searchsuggestionservice.service.SearchSuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SearchSuggestionController {

    private final SearchSuggestionService searchSuggestionService;

    @Autowired
    public SearchSuggestionController(SearchSuggestionService searchSuggestionService) {
        this.searchSuggestionService = searchSuggestionService;
    }

    @GetMapping("/search-suggestion")
    public ResponseEntity<List<SearchSuggestionItem>> getSuggestions(@RequestParam("q") String prefix) {
        List<String> suggestions = searchSuggestionService.getSuggestionsByPrefix(prefix);
        List<SearchSuggestionItem> items = suggestions.stream()
            .map(keyword -> new SearchSuggestionItem(keyword, keyword.toLowerCase().replace(" ", "-")))
            .collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }

    @GetMapping("/default-search-suggestion")
    public ResponseEntity<List<SearchSuggestionItem>> getDefaultSuggestions() {
        List<String> suggestions = searchSuggestionService.getDefaultSuggestions();
        List<SearchSuggestionItem> items = suggestions.stream()
            .map(keyword -> new SearchSuggestionItem(keyword, keyword.toLowerCase().replace(" ", "-")))
            .collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }
}
