package astr.api.core;

import astr.api.core.parameters.CloseApproachData;
import astr.api.core.parameters.EstimatedDiameter;

public record Asteroid(
        String neoReferenceId,
        String name,
        String nasaJplUrl,
        double absoluteMagnitudeH,
        EstimatedDiameter estimatedDiameter,
        boolean isPotentiallyHazardousAsteroid,
        CloseApproachData closeApproachData,
        boolean isSentryObject
) {
    public boolean isDangerous() {
        return isPotentiallyHazardousAsteroid
                && estimatedDiameter.maxMeters() > 300;
    }

    public boolean isFast() {
        return closeApproachData.relativeVelocity() > 50000;
    }
}
