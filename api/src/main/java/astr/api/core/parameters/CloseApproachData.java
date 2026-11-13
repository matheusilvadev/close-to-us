package astr.api.core.parameters;

/**
 * Dados de aproximação de um asteroide a um corpo celeste.
 *
 * @param closeApproachDate     Data da aproximação no formato YYYY-MM-DD
 * @param closeApproachDateFull Data e hora completas da aproximação (ex: "2025-Sep-07 12:34")
 * @param velocityKmS           Velocidade relativa em km/s
 * @param missDistanceKm        Distância de miss em quilômetros
 * @param orbitingBody          Corpo celeste de referência da aproximação (ex: "Earth")
 */
public record CloseApproachData(
        String closeApproachDate,
        String closeApproachDateFull,
        double velocityKmS,
        double missDistanceKm,
        String orbitingBody
) {
    public CloseApproachData(String closeApproachDateFull, double velocityKmS, double missDistanceKm, String orbitingBody) {
        this(null, closeApproachDateFull, velocityKmS, missDistanceKm, orbitingBody);
    }

    public CloseApproachData(String closeApproachDateFull, double velocityKmS, String orbitingBody) {
        this(null, closeApproachDateFull, velocityKmS, 0.0, orbitingBody);
    }
}
