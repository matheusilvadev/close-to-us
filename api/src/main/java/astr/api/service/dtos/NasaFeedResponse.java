package astr.api.service.dtos;

import astr.api.domain.AsteroidDomain;
import astr.api.service.mapper.Links;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class NasaFeedResponse {

    @JsonProperty("links")
    private Links links;

    @JsonProperty("element_count")
    private int elementCount;

    @JsonProperty("near_earth_objects")
    private Map<String, List<AsteroidDomain>> nearEarthObjects;

    public NasaFeedResponse() {
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public int getElementCount() {
        return elementCount;
    }

    public void setElementCount(int elementCount) {
        this.elementCount = elementCount;
    }

    public Map<String, List<AsteroidDomain>> getNearEarthObjects() {
        return nearEarthObjects;
    }

    public void setNearEarthObjects(Map<String, List<AsteroidDomain>> nearEarthObjects) {
        this.nearEarthObjects = nearEarthObjects;
    }
}
