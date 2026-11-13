package astr.api.core;

import astr.api.core.parameters.CloseApproachData;
import astr.api.core.parameters.EstimatedDiameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AsteroidTest {

    @Test
    @DisplayName("Should identify asteroid as dangerous strictly based on NASA isPotentiallyHazardousAsteroid flag")
    void shouldIdentifyDangerousAsteroid() {
        Asteroid asteroid = new Asteroid(
                "123",
                "Apophis",
                "http://nasa.gov",
                20.5,
                new EstimatedDiameter(50, 120),
                true,
                new CloseApproachData("2025-Sep-08 20:28", 22.5, 1200000.0, "Earth"),
                false);

        assertThat(asteroid.isDangerous()).isTrue();
    }

    @Test
    @DisplayName("Should not identify asteroid as dangerous when NASA flag is false even if diameter is large")
    void shouldNotIdentifyDangerousAsteroidWhenNotHazardous() {
        Asteroid asteroid = new Asteroid(
                "123",
                "Safe Asteroid",
                "https://nasa.gov",
                18.0,
                new EstimatedDiameter(400, 500),
                false,
                new CloseApproachData("2025-Sep-08 20:28", 15.0, 3000000.0, "Earth"),
                false);

        assertThat(asteroid.isDangerous()).isFalse();
    }

    @Test
    @DisplayName("Should identify asteroid as fast when relative velocity is greater than 20 km/s")
    void shouldIdentifyFastAsteroid() {
        Asteroid asteroid = new Asteroid(
                "123",
                "Fast Asteroid",
                "https://nasa.gov",
                18.0,
                new EstimatedDiameter(100, 200),
                false,
                new CloseApproachData("2025-Sep-08 20:28", 25.4, 4000000.0, "Earth"),
                false);

        assertThat(asteroid.isFast()).isTrue();
    }

    @Test
    @DisplayName("Should not identify asteroid as fast when relative velocity is lower than 20 km/s")
    void shouldNotIdentifyFastAsteroid() {
        Asteroid asteroid = new Asteroid(
                "123",
                "Slow Asteroid",
                "https://nasa.gov",
                18.0,
                new EstimatedDiameter(100, 200),
                false,
                new CloseApproachData("2025-Sep-08 20:28", 12.3, 5000000.0, "Earth"),
                false);

        assertThat(asteroid.isFast()).isFalse();
    }
}
