package com.mobvista.okr.vm;

import javax.validation.constraints.NotNull;

/**
 * View Model object for storing a user's credentials.
 */
public class AdjusterVM {

    @NotNull
    private Long adjusterId;

    @NotNull
    private Long seasonId;

    public Long getAdjusterId() {
        return adjusterId;
    }

    public void setAdjusterId(Long adjusterId) {
        this.adjusterId = adjusterId;
    }

    public Long getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Long seasonId) {
        this.seasonId = seasonId;
    }

    @Override
    public String toString() {
        return "AdjusterVM{" +
                "adjusterId=" + adjusterId +
                ", seasonId=" + seasonId +
                '}';
    }
}
