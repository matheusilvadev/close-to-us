package astr.api.controller;

import astr.api.service.GetCloseAsteroidsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/asteroids")
@CrossOrigin(origins = "*")
public class AsteroidController {
    private final GetCloseAsteroidsService nasaAsteroidService;

    public AsteroidController(GetCloseAsteroidsService nasaAsteroidService) {
        this.nasaAsteroidService = nasaAsteroidService;
    }

    @GetMapping("/summary")
    public Mono<List<AsteroidSummaryDTO>> getAsteroidSummary(
            @RequestParam("start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return nasaAsteroidService.getAsteroidFeedSummary(startDate, endDate);
    }
}
