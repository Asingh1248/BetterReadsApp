package io.javabrains.betterreads.search;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Controller
public class SearchController {

    private static final String COVER_IMAGE_ROOT = "https://covers.openlibrary.org/b/id/";
    private static final String NO_IMAGE_PATH = "/images/No-image.png";
    private static final int MAX_RESULTS = 10;
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private final WebClient webClient;

    public SearchController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(clientCodecConfigurer -> clientCodecConfigurer
                                .defaultCodecs()
                                .maxInMemorySize(8 * 1024 * 1024)) // Reduced from 16MB to 8MB
                        .build())
                .baseUrl("http://openlibrary.org/search.json")
                .build();
    }

    @GetMapping(value = "/search")
    public String getSearchResults(@RequestParam String query, Model model) {
        try {
            // Use non-blocking approach with timeout
            SearchResult result = this.webClient.get()
                    .uri("?q={query}", query)
                    .retrieve()
                    .bodyToMono(SearchResult.class)
                    .timeout(TIMEOUT)
                    .onErrorReturn(new SearchResult()) // Return empty result on error
                    .block();

            List<SearchResultBook> books = processSearchResults(result);
            model.addAttribute("searchResults", books);

        } catch (Exception e) {
            // Log error and provide empty results
            System.err.println("Search failed for query: " + query + ", Error: " + e.getMessage());
            model.addAttribute("searchResults", Collections.emptyList());
        }

        return "search";
    }

    private List<SearchResultBook> processSearchResults(SearchResult result) {
        if (result == null || result.getDocs() == null) {
            return Collections.emptyList();
        }

        return result.getDocs()
                .stream()
                .limit(MAX_RESULTS)
                .map(this::transformBookResult)
                .collect(Collectors.toList());
    }

    private SearchResultBook transformBookResult(SearchResultBook bookResult) {
        // Optimize key transformation
        String key = bookResult.getKey();
        if (key != null && key.startsWith("/works/")) {
            bookResult.setKey(key.substring(7)); // More efficient than replace
        }

        // Optimize cover image URL setting
        String coverId = bookResult.getCover_i();
        String coverUrl = StringUtils.hasText(coverId) 
                ? COVER_IMAGE_ROOT + coverId + "-M.jpg"
                : NO_IMAGE_PATH;
        
        bookResult.setCover_i(coverUrl);
        return bookResult;
    }
}
