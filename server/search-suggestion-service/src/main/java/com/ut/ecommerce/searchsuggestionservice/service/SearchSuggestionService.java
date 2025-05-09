package com.ut.ecommerce.searchsuggestionservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchSuggestionService {

    private final RestTemplate restTemplate;

    @Value("${common.data.service.url}")
    private String commonDataServiceUrl;

    @Autowired
    public SearchSuggestionService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Returns default suggestions when querying the common-data-service
     */
    public List<String> getDefaultSuggestions() {
        try {
            // Get the data from the common-data-service
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    commonDataServiceUrl + "/search-suggestion-list",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            // Check if the response is not null
            if (response.getBody() == null) {
                return new ArrayList<>();
            }

            // Get the body of the response as a Map
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("productKeywords")) {
                Object productKeywordsObj = responseBody.get("productKeywords");
                if (productKeywordsObj instanceof List<?>) {
                    return ((List<?>) productKeywordsObj).stream()
                            .filter(item -> item instanceof String)
                            .map(item -> (String) item)
                            .collect(Collectors.toList());
                }
            }
        } catch (Exception e) {
            System.err.println("Error while communicating with common-data-service: " + e.getMessage());
        }

        // Return an empty list in case of failure
        return new ArrayList<>();
    }

    /**
     * Filter suggestions based on a prefix
     */
    public List<String> getSuggestionsByPrefix(String prefix) {
        List<String> allSuggestions = getDefaultSuggestions();

        // Filter suggestions that start with the prefix (case-insensitive)
        return allSuggestions.stream()
                .filter(keyword -> keyword.toLowerCase().startsWith(prefix.toLowerCase()))
                .limit(10) // Limit the number of suggestions
                .collect(Collectors.toList());
    }
}
