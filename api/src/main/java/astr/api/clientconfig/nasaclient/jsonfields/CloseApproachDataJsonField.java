package astr.api.clientconfig.nasaclient.jsonfields;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CloseApproachDataJsonField(
        @JsonProperty("close_approach_date") String closeApproachDate,
        @JsonProperty("close_approach_date_full") String closeApproachDateFull,
        @JsonProperty("epoch_date_close_approach") Long epochDateCloseApproach,
        @JsonProperty("relative_velocity") RelativeVelocityDto relativeVelocity,
        @JsonProperty("miss_distance") MissDistanceDto missDistance,
        @JsonProperty("orbiting_body") String orbitingBody) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RelativeVelocityDto(
            @JsonProperty("kilometers_per_second") String kilometersPerSecond,
            @JsonProperty("kilometers_per_hour") String kilometersPerHour,
            @JsonProperty("miles_per_hour") String milesPerHour) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record MissDistanceDto(
            @JsonProperty("astronomical") String astronomical,
            @JsonProperty("lunar") String lunar,
            @JsonProperty("kilometers") String kilometers,
            @JsonProperty("miles") String miles) {
    }
}
