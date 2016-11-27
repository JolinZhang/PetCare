package com.github.jolinzhang.petcare.Adapter;



import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.jolinzhang.model.DataRepoConfig;
import com.github.jolinzhang.model.DataRepository;
import com.github.jolinzhang.model.Event;
import com.github.jolinzhang.petcare.R;

import org.w3c.dom.Text;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Jonelezhang on 11/23/16.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder>{

    private RealmResults<Event> events;

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


    public TimeLineAdapter() {
        super();


        DataRepository.getInstance().getPastEvents(new RealmChangeListener<RealmResults<Event>>() {
            @Override
            public void onChange(RealmResults<Event> element) {
                events = element;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public TimeLineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_line_item, parent, false);
        ViewHolder v = new ViewHolder(view);
        return v;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(events.get(position).getTitle());
        holder.content.setText(events.get(position).getDescription());
        holder.time.setText(events.get(position).getDatetime().toString());
    }


    @Override
    public int getItemCount() {
        if(events == null) return 0;
        return events.size();
    }
}
