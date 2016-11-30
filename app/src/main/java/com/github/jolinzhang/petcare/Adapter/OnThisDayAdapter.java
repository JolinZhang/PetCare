package com.github.jolinzhang.petcare.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.jolinzhang.petcare.R;

/**
 * Created by Jonelezhang on 11/30/16.
 */

public class OnThisDayAdapter extends RecyclerView.Adapter<OnThisDayAdapter.ViewHolder> {


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


    @Override
    public OnThisDayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_line_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OnThisDayAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
