package com.example.pamw.payload.request;

import javax.validation.constraints.NotBlank;

public class UpdateParcelStatusRequest {

    @NotBlank
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
