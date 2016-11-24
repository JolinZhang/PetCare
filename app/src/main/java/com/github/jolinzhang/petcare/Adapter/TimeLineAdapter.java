package com.github.jolinzhang.petcare.Adapter;



import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.jolinzhang.petcare.R;

/**
 * Created by Jonelezhang on 11/23/16.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView text;
        public ViewHolder(View view){
            super(view);
            text = (TextView) view.findViewById(R.id.text);
        }
    }

    public TimeLineAdapter() {
        super();
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

    }


    @Override
    public int getItemCount() {
        return 10;
    }
}
