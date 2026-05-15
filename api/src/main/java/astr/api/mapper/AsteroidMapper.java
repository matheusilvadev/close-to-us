package astr.api.mapper;


import astr.api.clientconfig.nasaclient.NasaFeedResponse;
import astr.api.clientconfig.nasaclient.jsonfields.CloseApproachDataJsonField;
import astr.api.clientconfig.nasaclient.jsonfields.NearEarthObjectsJsonField;
import astr.api.core.Asteroid;
import astr.api.core.parameters.CloseApproachData;
import astr.api.core.parameters.EstimatedDiameter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AsteroidMapper {

    public List<Asteroid> toDomainList(NasaFeedResponse response) {
        return response.nearEarthObjects()
                .values()
                .stream()
                .flatMap(List::stream)
                .map(this::toDomain)
                .toList();
    }

    public Asteroid toDomain(NearEarthObjectsJsonField dto) {
        CloseApproachDataJsonField approachDataDto = dto.closeApproachData()
                .stream()
                .findFirst()
                .orElse(null);

        return new Asteroid(
                dto.neoReferenceId(),
                dto.name(),
                dto.nasaJplUrl(),
                dto.absoluteMagnitudeH(),

                new EstimatedDiameter(
                        dto.estimatedDiameter()
                                .meters()
                                .estimatedDiameterMin(),

                        dto.estimatedDiameter()
                                .meters()
                                .estimatedDiameterMax()),

                dto.isPotentiallyHazardousAsteroid(),
                mapCloseApproachData(approachDataDto),
                dto.isSentryObject());
    }

    private CloseApproachData mapCloseApproachData(CloseApproachDataJsonField dto) {

        if (dto == null) {
            return new CloseApproachData(
                    "Unknown",
                    0,
                    "Unknown");
        }

        return new CloseApproachData(
                dto.closeApproachDateFull(),
                Double.parseDouble(
                        dto.relativeVelocity()
                                .milesPerHour()),
                dto.orbitingBody());
    }
}
