package astr.api.domain.closeapproachdata;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MissDistance {

    @JsonProperty("astronomical")
    private String astronomical;

    @JsonProperty("lunar")
    private String lunar;

    @JsonProperty("kilometers")
    private String kilometers;

    @JsonProperty("miles")
    private String  miles;

    public MissDistance() {
    }

    public String getAstronomical() {
        return astronomical;
    }

    public void setAstronomical(String astronomical) {
        this.astronomical = astronomical;
    }

    public String getLunar() {
        return lunar;
    }

    public void setLunar(String lunar) {
        this.lunar = lunar;
    }

    public String getKilometers() {
        return kilometers;
    }

    public void setKilometers(String kilometers) {
        this.kilometers = kilometers;
    }

    public String getMiles() {
        return miles;
    }

    public void setMiles(String miles) {
        this.miles = miles;
    }
}
