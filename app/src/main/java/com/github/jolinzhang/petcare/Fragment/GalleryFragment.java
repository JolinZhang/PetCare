package com.github.jolinzhang.petcare.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.jolinzhang.model.DataRepository;
import com.github.jolinzhang.model.Event;
import com.github.jolinzhang.petcare.Adapter.GalleryAdapter;
import com.github.jolinzhang.petcare.R;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Jonelezhang on 11/23/16.
 */

public class GalleryFragment extends Fragment {
    private RecyclerView galleryReclerView;
    private GalleryAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new GalleryAdapter();
    }

    /**
     *  Ru Zhang - rxz151130
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.gallery_fragment,container,false);

        //change fragment title
        getActivity().setTitle("Gallery");

        //Recycler
        galleryReclerView = (RecyclerView) view.findViewById(R.id.galleryRecycler);

//        StaggeredGridLayoutManager gridLayoutManager =
//                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        galleryReclerView.setLayoutManager(gridLayoutManager);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        galleryReclerView.setLayoutManager(layoutManager);

        galleryReclerView.setAdapter(adapter);

        DataRepository.getInstance().getPastEventsWithPicture(new RealmChangeListener<RealmResults<Event>>() {
            @Override
            public void onChange(RealmResults<Event> element) {
                adapter.pastEventsWithPicture = element;
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}
