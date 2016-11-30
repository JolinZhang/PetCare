package com.github.jolinzhang.petcare.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jolinzhang.model.DataRepoConfig;
import com.github.jolinzhang.petcare.Adapter.TimeLineAdapter;
import com.github.jolinzhang.petcare.MainActivity;
import com.github.jolinzhang.petcare.NewEventActivity;
import com.github.jolinzhang.petcare.R;

/**
 * Created by Jonelezhang on 11/23/16.
 */

public class TimeLineFragment extends Fragment{
    private RecyclerView timeLineRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.time_line_fragment, container,false);

        //change fragment title
        getActivity().setTitle("Time Line");

        //Recycler
        timeLineRecycler = (RecyclerView) view.findViewById(R.id.timeLineRecycler);
        layoutManager = new LinearLayoutManager(getActivity());
        timeLineRecycler.setLayoutManager(layoutManager);

        adapter = new TimeLineAdapter(view.getContext());
        timeLineRecycler.setAdapter(adapter);


        //Floating Action Bar action
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewEventActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
