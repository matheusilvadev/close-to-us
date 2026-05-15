package astr.api.clientconfig.nasaclient.jsonfields;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CloseApproachDataJsonField(@JsonProperty("close_approach_date_full") String closeApproachDateFull,
                                         @JsonProperty("relative_velocity") RelativeVelocityDto relativeVelocity,
                                         @JsonProperty("orbiting_body") String orbitingBody) {

    public record RelativeVelocityDto(@JsonProperty("miles_per_hour") String milesPerHour) {
    }
}
