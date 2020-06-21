package com.example.howmanydaysi.model;

import java.io.Serializable;
import java.util.Date;

public class EventEntity  {
    public String text;
    public int iconID;
    public int record_quantity;
    public int current_quantity;
    public EventEntity(String text, int iconID,  int current_quantity, int record_quantity)
    {
        this.text=text;
        this.iconID=iconID;
        this.record_quantity=record_quantity;
        this.current_quantity=current_quantity;
    }

}
