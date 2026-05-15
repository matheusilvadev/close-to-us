package astr.api.clientconfig.nasaclient;


import astr.api.clientconfig.exception.NasaApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;

@Configuration
public class NeoWsClient {

    private final RestClient restClient;

    @Value("${nasa.api.key)")
    private String apiKey;

    public NeoWsClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public NasaFeedResponse fetchNeoWsFeed(LocalDate startDate, LocalDate endDate) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/neo/rest/v1/feed")
                        .queryParam("start_date", startDate)
                        .queryParam("end_date", endDate)
                        .queryParam("api_key", apiKey)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        (request, response) -> {
                            throw new NasaApiException("Nasa client error");
                        })
                .onStatus(HttpStatusCode::is5xxServerError,
                        (request, response) -> {
                            throw new NasaApiException("NASA server unavailable");
                        })
                .body(NasaFeedResponse.class);

    }
}
