package com.github.jolinzhang.petcare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.jolinzhang.model.DataRepository;
import com.github.jolinzhang.model.Event;
import com.github.jolinzhang.petcare.EventActivity;
import com.github.jolinzhang.petcare.R;
import com.github.jolinzhang.util.Util;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Jonelezhang on 11/25/16.
 */

public class FutureAdapter extends RecyclerView.Adapter<FutureAdapter.ViewHolder>{

    private RealmResults<Event> futureEvents;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView dayTextView;
        private TextView monthTextView;
        public ViewHolder(View view){
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.future_item_title);
            descriptionTextView = (TextView) view.findViewById(R.id.future_item_description);
            monthTextView = (TextView) view.findViewById(R.id.future_item_month);
            dayTextView = (TextView) view.findViewById(R.id.future_item_day);
        }
    }

    public FutureAdapter(Context context) {
        this.context = context;
        DataRepository.getInstance().getFutureEvents(new RealmChangeListener<RealmResults<Event>>() {
            @Override
            public void onChange(RealmResults<Event> element) {
                futureEvents = element;
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public FutureAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.future_event_item, parent, false);
        FutureAdapter.ViewHolder v = new FutureAdapter.ViewHolder(view);
        return v;
    }

    @Override
    public void onBindViewHolder(FutureAdapter.ViewHolder holder, int position) {
        final Event thisEvent = futureEvents.get(position);
        holder.titleTextView.setText(thisEvent.getTitle());
        holder.descriptionTextView.setText(thisEvent.getDescription());
        holder.monthTextView.setText(Util.getInstance().getShortMonth(thisEvent.getDatetime()));
        holder.dayTextView.setText(Util.getInstance().getNumberDay(thisEvent.getDatetime()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventActivity.class);
                String event_id = thisEvent.getId();
                intent.putExtra("event_id", event_id);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        if(futureEvents == null) return 0;
        return futureEvents.size();
    }
}
