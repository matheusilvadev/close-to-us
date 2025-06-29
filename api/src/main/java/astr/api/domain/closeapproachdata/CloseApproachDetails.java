package astr.api.domain.closeapproachdata;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CloseApproachDetails {

    @JsonProperty("close_approach_date")
    private String closeApproachDate;

    @JsonProperty("close_approach_date_full")
    private String closeApproachDateFull;

    @JsonProperty("epoch_date_close_approach")
    private long epochDateCloseApproach;

    @JsonProperty("relative_velocity")
    private RelativeVelocity relativeVelocity;

    @JsonProperty("miss_distance")
    private MissDistance missDistance;

    public CloseApproachDetails() {
    }

    public String getCloseApproachDate() {
        return closeApproachDate;
    }

    public void setCloseApproachDate(String closeApproachDate) {
        this.closeApproachDate = closeApproachDate;
    }

    public String getCloseApproachDateFull() {
        return closeApproachDateFull;
    }

    public void setCloseApproachDateFull(String closeApproachDateFull) {
        this.closeApproachDateFull = closeApproachDateFull;
    }

    public long getEpochDateCloseApproach() {
        return epochDateCloseApproach;
    }

    public void setEpochDateCloseApproach(long epochDateCloseApproach) {
        this.epochDateCloseApproach = epochDateCloseApproach;
    }

    public RelativeVelocity getRelativeVelocity() {
        return relativeVelocity;
    }

    public void setRelativeVelocity(RelativeVelocity relativeVelocity) {
        this.relativeVelocity = relativeVelocity;
    }

    public MissDistance getMissDistance() {
        return missDistance;
    }

    public void setMissDistance(MissDistance missDistance) {
        this.missDistance = missDistance;
    }
}
