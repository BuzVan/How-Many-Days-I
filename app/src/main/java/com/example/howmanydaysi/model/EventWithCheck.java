package com.example.howmanydaysi.model;

public class EventWithCheck {
    private boolean check;
    private EventEntity eventEntity;

    public EventEntity getEventEntity() {
        return eventEntity;
    }

    public EventWithCheck(EventEntity eventEntity, boolean check)//делаем из eventEntity Event;
    //// Event используется когда пользователь отмечает выполнение
    {
        this.eventEntity=eventEntity;
        this.check=check;
    }
    public EventWithCheck(){}

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
