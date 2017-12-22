package ru.itis.romanov_andrey.perpenanto.security.states;

public enum ReservationState {

    COLLECTING("Собирается"), SENDED("Отправлен"), RECIEVED("Получен");

    private String status;

    ReservationState(String status){
        this.status = status;
    }

    @Override
    public String toString(){
        return this.status;
    }

}
