package com.github.jolinzhang.petcare.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jolinzhang.petcare.Adapter.OnThisDayAdapter;
import com.github.jolinzhang.petcare.R;

/**
 * Created by Jonelezhang on 11/29/16.
 */

public class OnThisDayFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new OnThisDayAdapter();
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




        return view;

    }
}
