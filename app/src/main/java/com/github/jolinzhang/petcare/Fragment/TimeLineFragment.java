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
import com.github.jolinzhang.model.DataRepository;
import com.github.jolinzhang.model.Event;
import com.github.jolinzhang.petcare.Adapter.TimeLineAdapter;
import com.github.jolinzhang.petcare.MainActivity;
import com.github.jolinzhang.petcare.NewEventActivity;
import com.github.jolinzhang.petcare.R;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Jonelezhang on 11/23/16.
 */

public class TimeLineFragment extends Fragment{
    private RecyclerView timeLineRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private TimeLineAdapter adapter;
    private FloatingActionButton fab;

    /**
     *  Ru Zhang - rxz151130
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new TimeLineAdapter(getActivity());
    }

    /**
     *  Ru Zhang - rxz151130
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.time_line_fragment, container,false);

        //change fragment title
        getActivity().setTitle("Time Line");

        //Recycler
        timeLineRecycler = (RecyclerView) view.findViewById(R.id.timeLineRecycler);
        layoutManager = new LinearLayoutManager(getActivity());
        timeLineRecycler.setLayoutManager(layoutManager);

        timeLineRecycler.setAdapter(adapter);

        //Floating Action Bar show
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        if(DataRepoConfig.getInstance().getCurrentPetId() == ""){
            fab.setVisibility(View.INVISIBLE);
        }else{
            fab.setVisibility(View.VISIBLE);
        }

        //Floating Action Bar action
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewEventActivity.class);
                startActivity(intent);
            }
        });

        DataRepository.getInstance().getPastEvents(new RealmChangeListener<RealmResults<Event>>() {
            @Override
            public void onChange(final RealmResults<Event> element) {
                timeLineRecycler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.events = element;
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        return view;
    }
}
