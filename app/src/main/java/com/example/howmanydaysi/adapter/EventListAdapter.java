package com.example.howmanydaysi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.howmanydaysi.R;
import com.example.howmanydaysi.model.Event;
import com.example.howmanydaysi.model.EventEntity;

import java.util.ArrayList;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter  {
    private static final int EXECUTION_TYPE=1;
    private static final int STATISTIC_TYPE=0;
    public boolean viewTypeActivity;
    public List<EventEntity> eventEntityList = new ArrayList<>();

    public EventListAdapter(boolean viewTypeActivity)
    {
        this.viewTypeActivity=viewTypeActivity;
    }

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == STATISTIC_TYPE)
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_item_statistic, parent, false);
        else
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_item_choice, parent, false);
        return new EventViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });
        ((EventViewHolder)holder).bind(eventEntityList.get(position));
    }

    @Override
    public int getItemCount() {
        return eventEntityList.size();
    }
    public int getItemViewType(int index){//Adapter вызывается в StatisticActivity и в EventExecutionActivity
        if (viewTypeActivity){//viewTypeActivity true если StatisticActivity
            return STATISTIC_TYPE; //viewTypeActivity false если EventExecutionActivity
        }
        else return EXECUTION_TYPE;
    }


}

