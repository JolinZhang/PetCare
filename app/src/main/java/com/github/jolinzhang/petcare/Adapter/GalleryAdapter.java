package com.github.jolinzhang.petcare.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.jolinzhang.model.DataRepository;
import com.github.jolinzhang.model.Event;
import com.github.jolinzhang.petcare.Fragment.GalleryFragment;
import com.github.jolinzhang.petcare.R;
import com.github.jolinzhang.util.Util;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Zengtai Qi - zxq150130 on 11/23/16.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    public RealmResults<Event> pastEventsWithPicture;

    /**
     * Zengtai Qi - zxq150130
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image1);
        }
    }

    /**
     * Zengtai Qi - zxq150130
     */
    public GalleryAdapter(){
//        DataRepository.getInstance().getPastEventsWithPicture(new RealmChangeListener<RealmResults<Event>>() {
//            @Override
//            public void onChange(RealmResults<Event> element) {
//                pastEventsWithPicture = element;
//                notifyDataSetChanged();
//            }
//        });
    }



    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_item,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.image.post(new Runnable() {
                    @Override
                    public void run() {
                        int width = holder.image.getMeasuredWidth();
                        int height = width;
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
                        holder.image.setLayoutParams(layoutParams);
                    }
                });

        Util.getInstance().loadImage(pastEventsWithPicture.get(position).getId(),holder.image,
                false);

    }

    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public int getItemCount() {
        if(pastEventsWithPicture == null)return 0;
        return pastEventsWithPicture.size();
    }


}
