package astr.api.clientconfig.nasaclient.jsonfields;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record NearEarthObjectsJsonField(
        String id,
        @JsonProperty("neo_reference_id") String neoReferenceId,
        String name,
        @JsonProperty("nasa_jpl_url") String nasaJplUrl,
        @JsonProperty("absolute_magnitude_h") double absoluteMagnitudeH,
        @JsonProperty("estimated_diameter") EstimatedDiameterJsonField estimatedDiameter,
        @JsonProperty("is_potentially_hazardous_asteroid") boolean isPotentiallyHazardousAsteroid,
        @JsonProperty("close_approach_data") List<CloseApproachDataJsonField> closeApproachData,
        @JsonProperty("is_sentry_object") boolean isSentryObject
) {
}
