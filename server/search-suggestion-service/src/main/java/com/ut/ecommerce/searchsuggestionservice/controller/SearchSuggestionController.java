package com.ut.ecommerce.searchsuggestionservice.controller;

import com.ut.ecommerce.searchsuggestionservice.model.SearchSuggestionResponse;
import com.ut.ecommerce.searchsuggestionservice.service.SearchSuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchSuggestionController {

    private final SearchSuggestionService searchSuggestionService;

    @Autowired
    public SearchSuggestionController(SearchSuggestionService searchSuggestionService) {
        this.searchSuggestionService = searchSuggestionService;
    }

    @GetMapping("/search-suggestion")
    public ResponseEntity<SearchSuggestionResponse> getSuggestions(@RequestParam("q") String prefix) {
        List<String> suggestions = searchSuggestionService.getSuggestionsByPrefix(prefix);
        return ResponseEntity.ok(new SearchSuggestionResponse(suggestions));
    }

    @GetMapping("/default-search-suggestion")
    public ResponseEntity<SearchSuggestionResponse> getDefaultSuggestions() {
        List<String> suggestions = searchSuggestionService.getDefaultSuggestions();
        return ResponseEntity.ok(new SearchSuggestionResponse(suggestions));
    }
}
