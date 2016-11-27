package com.github.jolinzhang.petcare.Adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.jolinzhang.petcare.R;

/**
 * Created by Jonelezhang on 11/25/16.
 */

public class FeatureAdapter  extends RecyclerView.Adapter<FeatureAdapter.ViewHolder>{
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView text;
        public ViewHolder(View view){
            super(view);
            text = (TextView) view.findViewById(R.id.text);
        }
    }

    public FeatureAdapter() {
        super();
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

    }


    @Override
    public int getItemCount() {
        return 10;
    }
}
