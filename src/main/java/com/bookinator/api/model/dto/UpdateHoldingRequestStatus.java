package com.bookinator.api.model.dto;

/**
 * Created by Sabina on 5/6/2018.
 */
public class UpdateHoldingRequestStatus {
    int statusId;
    int holdingRequestId;

    public UpdateHoldingRequestStatus() {
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getHoldingRequestId() {
        return holdingRequestId;
    }

    public void setHoldingRequestId(int holdingRequestId) {
        this.holdingRequestId = holdingRequestId;
    }
}
