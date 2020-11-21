package com.example.pamw.payload.response;

import com.example.pamw.entity.Parcel;

import java.util.List;

public class AllParcelsResponse {
    private final List<Parcel> parcelList;

    public AllParcelsResponse(List<Parcel> parcelList) {
        this.parcelList = parcelList;
    }

    public List<Parcel> getParcelList() {
        return parcelList;
    }
}
