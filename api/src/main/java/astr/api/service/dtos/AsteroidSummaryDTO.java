package astr.api.service.dtos;

public class AsteroidSummaryDTO {

    private String name;
    private boolean isPotentiallyHazardousAsteroid;
    private Units estimatedDiameterKilometers;
    private String missDistanceKilometers;
    private String relativeVelocityKilometersPerSecond;

    public AsteroidSummaryDTO() {
    }

    public AsteroidSummaryDTO(String name, boolean isPotentiallyHazardousAsteroid, Units estimatedDiameterKilometers, String missDistanceKilometers, String relativeVelocityKilometersPerSecond) {
        this.name = name;
        this.isPotentiallyHazardousAsteroid = isPotentiallyHazardousAsteroid;
        this.estimatedDiameterKilometers = estimatedDiameterKilometers;
        this.missDistanceKilometers = missDistanceKilometers;
        this.relativeVelocityKilometersPerSecond = relativeVelocityKilometersPerSecond;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPotentiallyHazardousAsteroid() {
        return isPotentiallyHazardousAsteroid;
    }

    public void setPotentiallyHazardousAsteroid(boolean potentiallyHazardousAsteroid) {
        isPotentiallyHazardousAsteroid = potentiallyHazardousAsteroid;
    }

    public Units getEstimatedDiameterKilometers() {
        return estimatedDiameterKilometers;
    }

    public void setEstimatedDiameterKilometers(Units estimatedDiameterKilometers) {
        this.estimatedDiameterKilometers = estimatedDiameterKilometers;
    }

    public String getMissDistanceKilometers() {
        return missDistanceKilometers;
    }

    public void setMissDistanceKilometers(String missDistanceKilometers) {
        this.missDistanceKilometers = missDistanceKilometers;
    }

    public String getRelativeVelocityKilometersPerSecond() {
        return relativeVelocityKilometersPerSecond;
    }

    public void setRelativeVelocityKilometersPerSecond(String relativeVelocityKilometersPerSecond) {
        this.relativeVelocityKilometersPerSecond = relativeVelocityKilometersPerSecond;
    }
}
