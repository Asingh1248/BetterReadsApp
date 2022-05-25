package io.javabrains.betterreads.search;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SearchController {

    private final String COVER_IMAGE_ROOT="https://covers.openlibrary.org/b/id/";

     private final WebClient webClient; //Most optimal way ==>Thread

    //Make Call to Search API-http://openlibrary.org/search.json?q=the+lord+of+the+rings
    public SearchController(WebClient.Builder webClientBuilder){

        this.webClient = webClientBuilder.exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(clientCodecConfigurer -> clientCodecConfigurer
                                .defaultCodecs()
                                .maxInMemorySize(16*1024*1024))
                                 .build()).baseUrl("http://openlibrary.org/search.json").build();

       //Scenario 2:
          //There was an unexpected error (type=Internal Server Error, status=500).
        //       200 OK from GET http://openlibrary.org/search.json?q=test; nested exception is org.springframework.core.io.buffer.DataBufferLimitException:
         // Exceeded limit on max bytes to buffer : 262144

    }


    //http://localhost:8080/search?query=test
    //Scenario : 404 Not Found from GET http://openlibrary.org/search.json?q=test
    //org.springframework.web.reactive.function.client.WebClientResponseException$NotFound: 404 Not Found from GET http://openlibrary.org/search.json?q=test
    //	at org.springframework.web.reactive.function.client.WebClientResponseException.create(WebClientResponseException.java:202)



    @GetMapping(value="/search")
    public String getSearchResults(@RequestParam String query, Model model){
        //Single request for
        Mono<SearchResult> resultsMono=this.webClient.get()
                .uri("?q={query}",query)
                .retrieve().bodyToMono(SearchResult.class);  //Whole String Payload is what got --In Futute I ill get it

        SearchResult result = resultsMono.block(); //Block the execution to get the results

        List<SearchResultBook> books = result.getDocs()
                .stream()
                .limit(10)
                .map(bookResult->{
                    bookResult.setKey(bookResult.getKey().replace("/works/",""));
                    String coverId=bookResult.getCover_i();
                    if(StringUtils.hasText(coverId)){
                        coverId=COVER_IMAGE_ROOT+coverId+"-M.jpg";
                    }else{
                        coverId="/images/No-image.png";
                    }
                    bookResult.setCover_i(coverId);
                    return bookResult; //Mapping something you need to return new thing ahich are u are mapping to
                })
                .collect(Collectors.toList());

        model.addAttribute("searchResults",books);


        return "search";
    }
}
