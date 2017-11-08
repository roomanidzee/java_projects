package com.romanidze.perpenanto.utils;

public enum ReservationStatus {

    PREPARING("Собирается"), SEND("Отправлен"), RECEIVED("Получен");

    private final String status;

    ReservationStatus(String status){
        this.status = status;
    }

    @Override
    public String toString(){
        return this.status;
    }

    public String getValue(){
        return this.status;
    }
}
