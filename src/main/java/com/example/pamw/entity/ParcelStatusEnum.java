package com.example.pamw.entity;

public enum ParcelStatusEnum {
    CREATED, IN_TRANSPORT, DELIVERED, PICKED_UP;

    public static String getName(ParcelStatusEnum e) {
        switch (e)
        {
            case CREATED:
                return "UTWORZONA";
            case IN_TRANSPORT:
                return "W DRODZE";
            case DELIVERED:
                return "DOSTARCZONA";
            case PICKED_UP:
                return "ODEBRANA";
        }
        return "STATUS_UNDEFINED";
    }
}

