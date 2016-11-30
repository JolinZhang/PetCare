package com.github.jolinzhang.petcare.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jolinzhang.model.DataRepository;
import com.github.jolinzhang.model.Event;
import com.github.jolinzhang.petcare.Adapter.OnThisDayAdapter;
import com.github.jolinzhang.petcare.R;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Jonelezhang on 11/29/16.
 */

public class OnThisDayFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private OnThisDayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new OnThisDayAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.onthisday_fragment,container, false);
        getActivity().setTitle("OnThisDay");

        //adapter
        recyclerView = (RecyclerView) view.findViewById(R.id.onThisDay_recycler);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        DataRepository.getInstance().getEventsOnThisDay(new RealmChangeListener<RealmResults<Event>>() {
            @Override
            public void onChange(final RealmResults<Event> element) {
                recyclerView.post(new Runnable() {
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
