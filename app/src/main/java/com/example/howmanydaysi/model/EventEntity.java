package com.example.howmanydaysi.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class EventEntity  {
    public String text;
    private int iconID;
    private int record_quantity;
    private int current_quantity;
    public EventEntity(String text, int iconID,  int current_quantity, int record_quantity)
    {
        this.text=text;
        this.iconID=iconID;
        this.record_quantity=record_quantity;
        this.current_quantity=current_quantity;
    }

    public int getIconID() {
        return iconID;
    }

    public int getCurrent_quantity() {
        return current_quantity;
    }

    public int getRecord_quantity() {
        return record_quantity;
    }

    public String getText() {
        return text;
    }
}
