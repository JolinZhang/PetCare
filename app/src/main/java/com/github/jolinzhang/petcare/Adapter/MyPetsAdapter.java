package com.github.jolinzhang.petcare.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jolinzhang.model.DataRepoConfig;
import com.github.jolinzhang.model.DataRepository;
import com.github.jolinzhang.model.Pet;
import com.github.jolinzhang.petcare.MyPetsActivity;
import com.github.jolinzhang.petcare.NewPetActivity;
import com.github.jolinzhang.petcare.R;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Zengtai Qi - zxq150130 on 11/29/16.
 */

public class MyPetsAdapter extends RecyclerView.Adapter<MyPetsAdapter.ViewHolder> {
    public RealmResults<Pet> pets;
    private Context context;


    /**
     * Zengtai Qi - zxq150130
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView petsName;
        private ImageView petsDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            petsName = (TextView) itemView.findViewById(R.id.pets_name);
            petsDelete = (ImageView) itemView.findViewById(R.id.pets_delete);
        }
    }

    /**
     * Zengtai Qi - zxq150130
     */
    public MyPetsAdapter(final Context context){
        this.context = context;
    }

    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public MyPetsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mypets_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.petsName.setText(pets.get(position).getName());
        holder.petsName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewPetActivity.class);
                intent.putExtra("pet_id",pets.get(position).getId());
                context.startActivity(intent);
            }
        });

        holder.petsDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataRepoConfig.getInstance().removePetId(pets.get(position).getId());
            }
        });
    }


    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public int getItemCount() {

        if(pets == null){return 0;}
        return pets.size();
    }



}
