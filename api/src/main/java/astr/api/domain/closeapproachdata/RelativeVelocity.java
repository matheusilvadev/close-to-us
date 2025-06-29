package astr.api.domain.closeapproachdata;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RelativeVelocity {

    @JsonProperty("kilometers_per_second")
    private String kilometersPerSecond;

    @JsonProperty("kilometers_per_hour")
    private String kilometersPerHour;

    @JsonProperty("miles_per_hour")
    private String milesPerHour;

    public RelativeVelocity() {
    }

    public String getKilometersPerSecond() {
        return kilometersPerSecond;
    }

    public void setKilometersPerSecond(String kilometersPerSecond) {
        this.kilometersPerSecond = kilometersPerSecond;
    }

    public String getKilometersPerHour() {
        return kilometersPerHour;
    }

    public void setKilometersPerHour(String kilometersPerHour) {
        this.kilometersPerHour = kilometersPerHour;
    }

    public String getMilesPerHour() {
        return milesPerHour;
    }

    public void setMilesPerHour(String milesPerHour) {
        this.milesPerHour = milesPerHour;
    }
}
