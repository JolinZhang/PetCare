package com.github.jolinzhang.petcare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jolinzhang.model.Event;
import com.github.jolinzhang.petcare.EventActivity;
import com.github.jolinzhang.petcare.R;
import com.github.jolinzhang.util.Util;

import io.realm.RealmResults;

/**
 * Created by Jonelezhang on 11/30/16.
 */

public class OnThisDayAdapter extends RecyclerView.Adapter<OnThisDayAdapter.ViewHolder> {

    public RealmResults<Event> events;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView content;
        private TextView time;
        private ImageView pictureImageView;
        public ViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
            time = (TextView) view.findViewById(R.id.time);
            pictureImageView = (ImageView) view.findViewById(R.id.timeline_item_picture);
        }

    }

    public OnThisDayAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public OnThisDayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_line_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final OnThisDayAdapter.ViewHolder holder, int position) {
        final Event thisEvent = events.get(position);
        if (thisEvent.hasPicture()) {
            holder.pictureImageView.setVisibility(View.VISIBLE);
            holder.pictureImageView.post(new Runnable() {
                @Override
                public void run() {
                    int width = holder.pictureImageView.getMeasuredWidth();
                    int height = width;
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
                    holder.pictureImageView.setLayoutParams(layoutParams);
                    Util.getInstance().loadImage(thisEvent.getId(), holder.pictureImageView, false);
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

    @Override
    public int getItemCount() {
        if(events == null) return 0;
        return events.size();
    }
}
