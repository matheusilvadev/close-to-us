package astr.api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;



@Schema(description = "Asteroid response payload")
public record AsteroidResponse(
        @Schema(description = "Asteroid name", example = "Apophis") String name,

        @Schema(description = "Maximum estimated diameter in meters", example = "450.5") double maxDiameterMeters,

        @Schema(description = "Indicates if asteroid is considered dangerous", example = "true") boolean dangerous,

        @Schema(description = "Indicates if asteroid velocity is high", example = "true") boolean fast,

        @Schema(description = "Celestial body being orbited", example = "Earth") String orbitingBody
) {
}
