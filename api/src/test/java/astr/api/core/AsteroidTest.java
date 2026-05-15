package astr.api.core;

import astr.api.core.parameters.CloseApproachData;
import astr.api.core.parameters.EstimatedDiameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AsteroidTest {

    @Test
    @DisplayName("Should identify asteroid as dangerous when hazardous and diameter is greater than 300 meters")
    void shouldIdentifyDangerousAsteroid() {

        Asteroid asteroid = new Asteroid(
                "123",
                "Apophis",
                "http://nasa.gov",
                20.5,
                new EstimatedDiameter(
                        250,
                        350),
                true,
                new CloseApproachData(
                        "2025-Sep-08 20:28",
                        45000,
                        "Earth"),
                false);

        boolean result = asteroid.isDangerous();
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should not identify asteroid as dangerous when diameter is less than 300 meters")
    void shouldNotIdentifyDangerousAsteroidWhenDiameterIsSmall() {

        Asteroid asteroid = new Asteroid(
                "123",
                "Small Asteroid",
                "https://nasa.gov",
                18.0,

                new EstimatedDiameter(
                        100,
                        250),

                true,

                new CloseApproachData(
                        "2025-Sep-08 20:28",
                        30000,
                        "Earth"),

                false);

        boolean result = asteroid.isDangerous();

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should not identify asteroid as dangerous when asteroid is not hazardous")
    void shouldNotIdentifyDangerousAsteroidWhenNotHazardous() {

        Asteroid asteroid = new Asteroid(
                "123",
                "Safe Asteroid",
                "https://nasa.gov",
                18.0,

                new EstimatedDiameter(
                        400,
                        500),

                false,

                new CloseApproachData(
                        "2025-Sep-08 20:28",
                        30000,
                        "Earth"),

                false);

        boolean result = asteroid.isDangerous();

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should identify asteroid as fast when velocity is greater than 50000 mph")
    void shouldIdentifyFastAsteroid() {

        Asteroid asteroid = new Asteroid(
                "123",
                "Fast Asteroid",
                "https://nasa.gov",
                18.0,

                new EstimatedDiameter(
                        100,
                        200),

                false,

                new CloseApproachData(
                        "2025-Sep-08 20:28",
                        65000,
                        "Earth"),

                false);

        boolean result = asteroid.isFast();

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should not identify asteroid as fast when velocity is lower than 50000 mph")
    void shouldNotIdentifyFastAsteroid() {

        Asteroid asteroid = new Asteroid(
                "123",
                "Slow Asteroid",
                "https://nasa.gov",
                18.0,

                new EstimatedDiameter(
                        100,
                        200),

                false,

                new CloseApproachData(
                        "2025-Sep-08 20:28",
                        25000,
                        "Earth"),

                false);

        boolean result = asteroid.isFast();

        assertThat(result).isFalse();
    }

}