package com.ut.ecommerce.searchsuggestionservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchSuggestionItem {
    private String keyword;
    private String link;
}
