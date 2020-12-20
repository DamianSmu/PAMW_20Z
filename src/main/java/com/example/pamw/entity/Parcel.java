package com.example.pamw.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class Parcel implements Serializable {
    @Id
    private String id = UUID.randomUUID().toString();

    @NotNull
    private String senderName;

    @NotNull
    private String receiver;

    @NotNull
    private String postOffice;

    @NotNull
    private String size;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ParcelStatusEnum status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User sender;

    public Parcel(String receiver, String postOffice, String size, User sender) {
        this.receiver = receiver;
        this.postOffice = postOffice;
        this.size = size;
        this.sender = sender;
        this.senderName = sender.getUsername();
        this.status = ParcelStatusEnum.CREATED;
    }

    public Parcel() {

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

    public ParcelStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ParcelStatusEnum status) {
        this.status = status;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
