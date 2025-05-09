package com.ut.ecommerce.searchsuggestionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a search suggestion item returned by the API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchSuggestionItem {
    private String keyword; // The suggested keyword
    private String url; // The corresponding URL (slug for routing)
}
