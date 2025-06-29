package astr.api.domain;

import astr.api.domain.closeapproachdata.CloseApproachDetails;
import astr.api.domain.estdiameter.EstimatedDiameter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AsteroidDomain {

    @JsonProperty("id")
    private String id;

    @JsonProperty("neo_reference_id")
    private String neoReferenceId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("nasa_jpl_url")
    private String nasaJplUrl;

    @JsonProperty("absolute_magnitude_h")
    private double absoluteMagnitudeH;

    @JsonProperty("estimated_diameter")
    private EstimatedDiameter estimatedDiameter;

    @JsonProperty("is_potentially_hazardous_asteroid")
    private boolean isPotentiallyHazardousAsteroid;

    @JsonProperty("close_approach_data")
    private List<CloseApproachDetails> closeApproachData;

    @JsonProperty("orbiting_body")
    private String orbitingBody;

    @JsonProperty("is_sentry_object")
    private boolean isSentryObject;

    public AsteroidDomain() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNeoReferenceId() {
        return neoReferenceId;
    }

    public void setNeoReferenceId(String neoReferenceId) {
        this.neoReferenceId = neoReferenceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNasaJplUrl() {
        return nasaJplUrl;
    }

    public void setNasaJplUrl(String nasaJplUrl) {
        this.nasaJplUrl = nasaJplUrl;
    }

    public double getAbsoluteMagnitudeH() {
        return absoluteMagnitudeH;
    }

    public void setAbsoluteMagnitudeH(double absoluteMagnitudeH) {
        this.absoluteMagnitudeH = absoluteMagnitudeH;
    }

    public EstimatedDiameter getEstimatedDiameter() {
        return estimatedDiameter;
    }

    public void setEstimatedDiameter(EstimatedDiameter estimatedDiameter) {
        this.estimatedDiameter = estimatedDiameter;
    }

    public boolean isPotentiallyHazardousAsteroid() {
        return isPotentiallyHazardousAsteroid;
    }

    public void setPotentiallyHazardousAsteroid(boolean potentiallyHazardousAsteroid) {
        isPotentiallyHazardousAsteroid = potentiallyHazardousAsteroid;
    }

    public List<CloseApproachDetails> getCloseApproachData() {
        return closeApproachData;
    }

    public void setCloseApproachData(List<CloseApproachDetails> closeApproachData) {
        this.closeApproachData = closeApproachData;
    }

    public String getOrbitingBody() {
        return orbitingBody;
    }

    public void setOrbitingBody(String orbitingBody) {
        this.orbitingBody = orbitingBody;
    }

    public boolean isSentryObject() {
        return isSentryObject;
    }

    public void setSentryObject(boolean sentryObject) {
        isSentryObject = sentryObject;
    }
}
