package com.example.howmanydaysi.adapter;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.howmanydaysi.R;
import com.example.howmanydaysi.model.EventEntity;
import com.example.howmanydaysi.service.WordsForm;

public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    protected TextView eventTextView;
    protected TextView quantityTextView;
    protected ImageView iconImageView;
    protected CheckBox checkBox;
    private static final int EXECUTION_TYPE=1;
    private static final int STATISTIC_TYPE=0;

    public EventViewHolder(@NonNull View itemView, int viewType) {
        super(itemView);

        eventTextView = itemView.findViewById(R.id.event_text);
        iconImageView=itemView.findViewById(R.id.icon);
        quantityTextView = itemView.findViewById(R.id.quantity_days);
        if(viewType==EXECUTION_TYPE) {
            checkBox = itemView.findViewById(R.id.checkBox);
        }
        else {
            itemView.setOnCreateContextMenuListener(this);
        }


    }
    public void bind(EventEntity eventEntity){
        eventTextView.setText(eventEntity.text);
        if(quantityTextView!=null)
            quantityTextView.setText(
                    eventEntity.getCurrent_quantity()+ " " + WordsForm.getDayWordForm(eventEntity.getCurrent_quantity()));
        iconImageView.setImageResource(eventEntity.getIconID());
    }
    //контекстное меню для элемента RecyclerView
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.add(Menu.NONE, R.id.change, Menu.NONE, R.string.change_name);

        menu.add(Menu.NONE, R.id.delete, Menu.NONE, R.string.delete_name);
    }
}
