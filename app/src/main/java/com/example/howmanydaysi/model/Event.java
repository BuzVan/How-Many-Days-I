package com.example.howmanydaysi.model;

public class Event {
    public boolean check;
    public EventEntity eventEntity;
    public Event(EventEntity eventEntity, boolean check)//делаем из eventEntity Event;
    //// Event используется когда пользователь отмечает выполнение
    {
        this.eventEntity=eventEntity;
        this.check=check;
    }
    public  Event(){}
}
