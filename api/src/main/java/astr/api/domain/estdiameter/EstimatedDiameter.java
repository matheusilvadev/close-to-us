package astr.api.domain.estdiameter;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EstimatedDiameter {

    @JsonProperty("kilometers")
    private Units kilometers;

    @JsonProperty("meters")
    private Units meters;

    @JsonProperty("miles")
    private Units miles;

    @JsonProperty("feet")
    private Units feet;

    public EstimatedDiameter() {
    }

    public Units getKilometers() {
        return kilometers;
    }

    public void setKilometers(Units kilometers) {
        this.kilometers = kilometers;
    }

    public Units getMeters() {
        return meters;
    }

    public void setMeters(Units meters) {
        this.meters = meters;
    }

    public Units getMiles() {
        return miles;
    }

    public void setMiles(Units miles) {
        this.miles = miles;
    }

    public Units getFeet() {
        return feet;
    }

    public void setFeet(Units feet) {
        this.feet = feet;
    }

}
