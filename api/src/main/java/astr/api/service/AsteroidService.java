package astr.api.service;

import astr.api.domain.AsteroidDomain;
import astr.api.service.dtos.AsteroidSummaryDTO;
import astr.api.service.dtos.NasaFeedResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AsteroidService {

    private final WebClient webClient;

    public AsteroidService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<List<AsteroidSummaryDTO>> getAsteroidFeedSummary(LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateStr = startDate.format(formatter);
        String endDateStr = endDate.format(formatter);

        // A chave da API já é adicionada automaticamente pelo WebClientConfig
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/neo/rest/v1/feed")
                        .queryParam("start_date", startDateStr)
                        .queryParam("end_date", endDateStr)
                        .build())
                .retrieve()
                .bodyToMono(NasaFeedResponse.class)
                .map(response -> {
                    List<AsteroidSummaryDTO> summaryList = new ArrayList<>();
                    response.getNearEarthObjects().forEach((date, asteroids) ->
                            asteroids.stream()
                                    .map(this::mapToAsteroidSummaryDTO)
                                    .forEach(summaryList::add)
                    );
                    return summaryList;
                });
    }

    private AsteroidSummaryDTO mapToAsteroidSummaryDTO(AsteroidDomain asteroid) {
        // Assume que sempre haverá pelo menos um close_approach_data
        String missDistanceKilometers = null;
        String relativeVelocityKilometersPerSecond = null;

        if (asteroid.getCloseApproachData() != null && !asteroid.getCloseApproachData().isEmpty()) {
            missDistanceKilometers = asteroid.getCloseApproachData().get(0).getMissDistance().getKilometers();
            relativeVelocityKilometersPerSecond = asteroid.getCloseApproachData().get(0).getRelativeVelocity().getKilometersPerSecond();
        }

        return new AsteroidSummaryDTO(
                asteroid.getName(),
                asteroid.isPotentiallyHazardousAsteroid(),
                asteroid.getEstimatedDiameter() != null ? asteroid.getEstimatedDiameter().getKilometers() : null,
                missDistanceKilometers,
                relativeVelocityKilometersPerSecond
        );
    }
}
