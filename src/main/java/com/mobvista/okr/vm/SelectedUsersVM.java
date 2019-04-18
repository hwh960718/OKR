package com.mobvista.okr.vm;

import javax.validation.constraints.NotNull;

/**
 * View Model object for storing a user's credentials.
 */
public class SelectedUsersVM {

    @NotNull
    private Long selectedUserId;

    private Long seasonId;

    private Long userSeasonId;

    public Long getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(Long selectedUserId) {
        this.selectedUserId = selectedUserId;
    }

    public Long getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Long seasonId) {
        this.seasonId = seasonId;
    }

    public Long getUserSeasonId() {
        return userSeasonId;
    }

    public void setUserSeasonId(Long userSeasonId) {
        this.userSeasonId = userSeasonId;
    }

    @Override
    public String toString() {
        return "AdjusterVM{" +
                "selectedUserId=" + selectedUserId +
                ", seasonId=" + seasonId +
                '}';
    }
}
