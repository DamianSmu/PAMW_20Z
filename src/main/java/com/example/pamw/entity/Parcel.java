package com.example.pamw.entity;

import nonapi.io.github.classgraph.json.Id;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

public class Parcel implements Serializable {
    @Id
    private String id = UUID.randomUUID().toString();

    @NotNull
    private String receiver;

    @NotNull
    private String postOffice;

    @NotNull
    private String size;

    public Parcel(@NotNull String receiver, @NotNull String postOffice, @NotNull String size) {
        this.receiver = receiver;
        this.postOffice = postOffice;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getPostOffice() {
        return postOffice;
    }

    public void setPostOffice(String postOffice) {
        this.postOffice = postOffice;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
