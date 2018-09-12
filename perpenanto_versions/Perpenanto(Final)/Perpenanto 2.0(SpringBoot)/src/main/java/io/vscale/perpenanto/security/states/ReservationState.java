package io.vscale.perpenanto.security.states;

public enum ReservationState {

    COLLECTING("Собирается"), SENDED("Отправлен"), RECEIVED("Получен");

    private String status;

    ReservationState(String status){
        this.status = status;
    }

    @Override
    public String toString(){
        return this.status;
    }

}
