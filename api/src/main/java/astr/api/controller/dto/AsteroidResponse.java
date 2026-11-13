package astr.api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payload de resposta de um asteroide próximo da Terra")
public record AsteroidResponse(

        @Schema(description = "Designação oficial do asteroide", example = "2014 XQ3")
        String designation,

        @Schema(description = "Nome do asteroide fornecido pela NASA", example = "(2014 XQ3)")
        String name,

        @Schema(description = "Diâmetro estimado máximo em metros", example = "125.45")
        double maxDiameterMeters,

        @Schema(description = "Velocidade relativa de aproximação em km/s", example = "18.42")
        double velocityKmS,

        @Schema(description = "Distância de aproximação (miss distance) em quilômetros", example = "1234567.89")
        double missDistanceKm,

        @Schema(description = "Data da aproximação no formato YYYY-MM-DD", example = "2025-09-08")
        String closeApproachDate,

        @Schema(description = "Corpo celeste de referência da aproximação", example = "Earth")
        String orbitingBody,

        @Schema(description = "Flag isPotentiallyHazardousAsteroid da NASA — sem critérios adicionais", example = "false")
        boolean dangerous
) {
}
