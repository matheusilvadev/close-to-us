package astr.api.clientconfig.nasaclient.jsonfields;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EstimatedDiameterJsonField(MetersDto meters) {
    public record MetersDto(
            @JsonProperty("estimated_diameter_min") double estimatedDiameterMin,
            @JsonProperty("estimated_diameter_max") double estimatedDiameterMax) {
    }
}
