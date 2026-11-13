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
        if (response == null || response.nearEarthObjects() == null) {
            return List.of();
        }

        return response.nearEarthObjects()
                .entrySet()
                .stream()
                .flatMap(entry -> {
                    String approachDate = entry.getKey();
                    return entry.getValue()
                            .stream()
                            .map(dto -> toDomain(dto, approachDate));
                })
                .toList();
    }

    public Asteroid toDomain(NearEarthObjectsJsonField dto) {
        return toDomain(dto, null);
    }

    public Asteroid toDomain(NearEarthObjectsJsonField dto, String targetDate) {
        CloseApproachDataJsonField approachDataDto = selectCloseApproachData(dto.closeApproachData(), targetDate);

        double minMeters = 0.0;
        double maxMeters = 0.0;
        if (dto.estimatedDiameter() != null && dto.estimatedDiameter().meters() != null) {
            minMeters = dto.estimatedDiameter().meters().estimatedDiameterMin();
            maxMeters = dto.estimatedDiameter().meters().estimatedDiameterMax();
        }

        String designation = (dto.designation() != null && !dto.designation().isBlank())
                ? dto.designation().trim()
                : dto.name();

        return new Asteroid(
                dto.neoReferenceId(),
                dto.name(),
                designation,
                dto.nasaJplUrl(),
                dto.absoluteMagnitudeH(),
                new EstimatedDiameter(minMeters, maxMeters),
                dto.isPotentiallyHazardousAsteroid(),
                mapCloseApproachData(approachDataDto),
                dto.isSentryObject());
    }

    /**
     * Seleciona o registro de aproximação correspondente à data informada,
     * garantindo que relative_velocity, miss_distance e orbiting_body pertençam
     * ao mesmo evento sem misturar aproximações distintas.
     */
    private CloseApproachDataJsonField selectCloseApproachData(
            List<CloseApproachDataJsonField> list,
            String targetDate
    ) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        if (targetDate != null && !targetDate.isBlank()) {
            for (CloseApproachDataJsonField item : list) {
                if (targetDate.equals(item.closeApproachDate())) {
                    return item;
                }
            }
        }

        return list.get(0);
    }

    private CloseApproachData mapCloseApproachData(CloseApproachDataJsonField dto) {
        if (dto == null) {
            return new CloseApproachData(null, "Unknown", 0.0, 0.0, "Unknown");
        }

        double velocityKmS = parseVelocityKmS(dto);
        double missDistanceKm = parseMissDistanceKm(dto);

        return new CloseApproachData(
                dto.closeApproachDate(),
                dto.closeApproachDateFull(),
                velocityKmS,
                missDistanceKm,
                dto.orbitingBody() != null ? dto.orbitingBody() : "Unknown");
    }

    /**
     * Extrai a velocidade relativa em km/s.
     * Prioridade: kilometers_per_second -> kilometers_per_hour / 3600 -> 0.0
     */
    private double parseVelocityKmS(CloseApproachDataJsonField dto) {
        if (dto.relativeVelocity() == null) return 0.0;

        String kmS = dto.relativeVelocity().kilometersPerSecond();
        if (kmS != null && !kmS.isBlank()) {
            try {
                return Double.parseDouble(kmS);
            } catch (NumberFormatException ignored) {
            }
        }

        String kmH = dto.relativeVelocity().kilometersPerHour();
        if (kmH != null && !kmH.isBlank()) {
            try {
                return Double.parseDouble(kmH) / 3600.0;
            } catch (NumberFormatException ignored) {
            }
        }

        String mph = dto.relativeVelocity().milesPerHour();
        if (mph != null && !mph.isBlank()) {
            try {
                return Double.parseDouble(mph) * 0.00044704;
            } catch (NumberFormatException ignored) {
            }
        }

        return 0.0;
    }

    /**
     * Extrai a distância de aproximação (miss distance) em quilômetros.
     */
    private double parseMissDistanceKm(CloseApproachDataJsonField dto) {
        if (dto.missDistance() == null) return 0.0;

        String km = dto.missDistance().kilometers();
        if (km != null && !km.isBlank()) {
            try {
                return Double.parseDouble(km);
            } catch (NumberFormatException ignored) {
            }
        }

        return 0.0;
    }
}
