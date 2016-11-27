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

import com.github.jolinzhang.petcare.Adapter.GalleryAdapter;
import com.github.jolinzhang.petcare.R;

/**
 * Created by Jonelezhang on 11/23/16.
 */

public class GalleryFragment extends Fragment {
    private RecyclerView galleryReclerView;
    private RecyclerView.Adapter adapter;
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

        adapter = new GalleryAdapter();
        galleryReclerView.setAdapter(adapter);

        return view;
    }
}
