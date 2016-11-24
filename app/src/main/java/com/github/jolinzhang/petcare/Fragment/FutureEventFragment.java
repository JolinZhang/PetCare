package com.github.jolinzhang.petcare.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jolinzhang.petcare.Adapter.TimeLineAdapter;
import com.github.jolinzhang.petcare.R;

/**
 * Created by Jonelezhang on 11/23/16.
 */

public class FutureEventFragment extends Fragment {
    private RecyclerView futureEventRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.future_event_fragment,container, false);
        futureEventRecycler = (RecyclerView) view.findViewById(R.id.futureEventRecycler);
        layoutManager = new LinearLayoutManager(getActivity());
        futureEventRecycler.setLayoutManager(layoutManager);
        adapter = new TimeLineAdapter();
        futureEventRecycler.setAdapter(adapter);
        return view;
    }
}