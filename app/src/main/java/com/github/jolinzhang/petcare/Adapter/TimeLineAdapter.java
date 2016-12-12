package com.github.jolinzhang.petcare.Adapter;



import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.jolinzhang.model.DataRepository;
import com.github.jolinzhang.model.Event;
import com.github.jolinzhang.petcare.EventActivity;
import com.github.jolinzhang.petcare.R;
import com.github.jolinzhang.util.Util;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Zengtai Qi - zxq150130 on 11/23/16.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder>{

    public RealmResults<Event> events;
    private Context context;

    /**
     * Zengtai Qi - zxq150130
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView content;
        private TextView time;
        private ImageView pictureImageView;
        private ProgressBar processBar;
        public ViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
            time = (TextView) view.findViewById(R.id.time);
            pictureImageView = (ImageView) view.findViewById(R.id.timeline_item_picture);
            processBar = (ProgressBar) view.findViewById(R.id.process_bar);
        }
    }


    /**
     * Zengtai Qi - zxq150130
     */
    public TimeLineAdapter(Context context) {
        super();
        this.context = context;
//        DataRepository.getInstance().getPastEvents(new RealmChangeListener<RealmResults<Event>>() {
//            @Override
//            public void onChange(RealmResults<Event> element) {
//                events = element;
//                notifyDataSetChanged();
//            }
//        });
    }

    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public TimeLineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_line_item, parent, false);
        ViewHolder v = new ViewHolder(view);
        return v;
    }

    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Event thisEvent = events.get(position);
        if (thisEvent.hasPicture()) {
            holder.pictureImageView.setVisibility(View.VISIBLE);
            holder.pictureImageView.post(new Runnable() {
                @Override
                public void run() {
                    Util.getInstance().loadImage(thisEvent.getId(), holder.pictureImageView, false, new com.squareup.picasso.Callback(){
                        @Override
                        public void onSuccess() {
                            holder.processBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                            holder.processBar.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });
        } else {
            holder.pictureImageView.setVisibility(View.GONE);
        }


        holder.title.setText(thisEvent.getTitle());
        holder.content.setText(thisEvent.getDescription());
        holder.time.setText(Util.getInstance().dateFormatter().format(thisEvent.getDatetime()));
        //item click issue
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventActivity.class);
                String event_id = thisEvent.getId();
                intent.putExtra("event_id",event_id);
                context.startActivity(intent);
            }
        });
    }


    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public int getItemCount() {
        if(events == null) return 0;
        return events.size();
    }
}
