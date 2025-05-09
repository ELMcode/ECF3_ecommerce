package com.ut.ecommerce.searchsuggestionservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchSuggestionResponse {
    private List<String> suggestions;
}
