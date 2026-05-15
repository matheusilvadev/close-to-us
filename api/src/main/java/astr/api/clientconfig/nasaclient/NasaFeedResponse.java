package astr.api.clientconfig.nasaclient;

import astr.api.clientconfig.nasaclient.jsonfields.LinksJsonField;
import astr.api.clientconfig.nasaclient.jsonfields.NearEarthObjectsJsonField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record NasaFeedResponse(
        LinksJsonField links,
        @JsonProperty("element_count") int elementCount,
        @JsonProperty("near_earth_objects") Map<String, List<NearEarthObjectsJsonField>> nearEarthObjects
) {
}
