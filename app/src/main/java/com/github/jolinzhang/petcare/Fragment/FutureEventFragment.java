package com.github.jolinzhang.petcare.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jolinzhang.petcare.Adapter.FutureAdapter;
import com.github.jolinzhang.petcare.NewEventActivity;
import com.github.jolinzhang.petcare.R;

import static android.widget.LinearLayout.VERTICAL;

/**
 * Created by Jonelezhang on 11/23/16.
 */

public class FutureEventFragment extends Fragment {
    private RecyclerView futureEventRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new FutureAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.future_event_fragment,container, false);
        //change fragment title
        getActivity().setTitle("Future Event");

        //Recycler
        futureEventRecycler = (RecyclerView) view.findViewById(R.id.futureEventRecycler);
        layoutManager = new LinearLayoutManager(getActivity());
        futureEventRecycler.setLayoutManager(layoutManager);
        futureEventRecycler.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(futureEventRecycler.getContext(),
                VERTICAL);
        futureEventRecycler.addItemDecoration(dividerItemDecoration);

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
