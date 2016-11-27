package com.github.jolinzhang.petcare.Adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.jolinzhang.model.DataRepository;
import com.github.jolinzhang.model.Event;
import com.github.jolinzhang.petcare.R;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Jonelezhang on 11/25/16.
 */

public class FeatureAdapter  extends RecyclerView.Adapter<FeatureAdapter.ViewHolder>{

    private RealmResults<Event> futureEvents;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView content;
        private TextView time;
        public ViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
            time = (TextView) view.findViewById(R.id.time);

        }
    }

    public FeatureAdapter() {
        super();
        DataRepository.getInstance().getFutureEvents(new RealmChangeListener<RealmResults<Event>>() {
            @Override
            public void onChange(RealmResults<Event> element) {
                futureEvents = element;
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public FeatureAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.future_event_item, parent, false);
        FeatureAdapter.ViewHolder v = new FeatureAdapter.ViewHolder(view);
        return v;
    }

    @Override
    public void onBindViewHolder(FeatureAdapter.ViewHolder holder, int position) {
        holder.title.setText(futureEvents.get(position).getTitle());
        holder.content.setText(futureEvents.get(position).getDescription());
        holder.time.setText(futureEvents.get(position).getDatetime().toString());

    }


    @Override
    public int getItemCount() {
        if(futureEvents == null) return 0;
        return futureEvents.size();
    }
}
