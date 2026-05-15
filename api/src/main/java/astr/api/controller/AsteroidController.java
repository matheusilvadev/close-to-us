package astr.api.controller;

import astr.api.controller.dto.AsteroidResponse;
import astr.api.service.GetCloseAsteroidsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Asteroids", description = "Operations related to near-earth asteroids")
@RestController
@RequestMapping("api/v1/asteroids")
public class AsteroidController {

    private final GetCloseAsteroidsService useCase;

    public AsteroidController(GetCloseAsteroidsService useCase) {
        this.useCase = useCase;
    }

    @Operation(summary = "Get close asteroids", description = """
                        Fetches asteroids from NASA NeoWs API
                        based on a start and end date.
                        """)
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Asteroids fetched successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AsteroidResponse.class)))),

            @ApiResponse(responseCode = "400", description = "Invalid date parameters"),

            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping()
    public List<AsteroidResponse> getAsteroids(

            @Parameter(description = "Start date in yyyy-MM-dd format", example = "2025-09-07") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "End date in yyyy-MM-dd format", example = "2025-09-08") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return useCase.execute(startDate, endDate)
                .stream()
                .map(asteroid -> new AsteroidResponse(
                        asteroid.name(),

                        asteroid.estimatedDiameter()
                                .maxMeters(),

                        asteroid.isDangerous(),

                        asteroid.isFast(),

                        asteroid.closeApproachData()
                                .orbitingBody()))
                .toList();
    }

}
