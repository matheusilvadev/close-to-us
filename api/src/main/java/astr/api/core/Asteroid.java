package astr.api.core;

import astr.api.core.parameters.CloseApproachData;
import astr.api.core.parameters.EstimatedDiameter;

public record Asteroid(
        String neoReferenceId,
        String name,
        String designation,
        String nasaJplUrl,
        double absoluteMagnitudeH,
        EstimatedDiameter estimatedDiameter,
        boolean isPotentiallyHazardousAsteroid,
        CloseApproachData closeApproachData,
        boolean isSentryObject
) {
    public Asteroid(
            String neoReferenceId,
            String name,
            String nasaJplUrl,
            double absoluteMagnitudeH,
            EstimatedDiameter estimatedDiameter,
            boolean isPotentiallyHazardousAsteroid,
            CloseApproachData closeApproachData,
            boolean isSentryObject
    ) {
        this(
                neoReferenceId,
                name,
                name,
                nasaJplUrl,
                absoluteMagnitudeH,
                estimatedDiameter,
                isPotentiallyHazardousAsteroid,
                closeApproachData,
                isSentryObject
        );
    }

    /**
     * Retorna true exclusivamente com base no flag isPotentiallyHazardousAsteroid
     * fornecido pela NASA — sem critérios adicionais de diâmetro ou velocidade.
     */
    public boolean isDangerous() {
        return isPotentiallyHazardousAsteroid;
    }

    /**
     * Asteroide considerado rápido se sua velocidade relativa superar 20 km/s.
     * Referência: a velocidade média de impacto de asteroides é ~17-20 km/s.
     */
    public boolean isFast() {
        return closeApproachData != null && closeApproachData.velocityKmS() > 20.0;
    }
}
