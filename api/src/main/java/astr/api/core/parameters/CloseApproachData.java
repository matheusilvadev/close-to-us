package astr.api.core.parameters;

public record CloseApproachData(
        String closeApproachDateFull,
        double relativeVelocity, // miles per hour
        String orbitingBody
) {
}
