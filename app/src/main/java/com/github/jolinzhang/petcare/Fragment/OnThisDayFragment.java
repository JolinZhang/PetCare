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

import java.util.Calendar;
import java.util.Date;

import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Jonelezhang on 11/29/16.
 */

public class OnThisDayFragment extends Fragment {

    private RecyclerView onThisDayRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private OnThisDayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new OnThisDayAdapter(getActivity());
    }

    /**
     *  Ru Zhang - rxz151130
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.onthisday_fragment,container, false);
        getActivity().setTitle("OnThisDay");

        //adapter
        onThisDayRecycler = (RecyclerView) view.findViewById(R.id.onThisDay_recycler);

        layoutManager = new LinearLayoutManager(getActivity());
        onThisDayRecycler.setLayoutManager(layoutManager);

        onThisDayRecycler.setAdapter(adapter);

        Date date = new Date();
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int month = cal.get(Calendar.MONTH);
        final int day = cal.get(Calendar.DAY_OF_MONTH);
        DataRepository.getInstance().getPastEvents(new RealmChangeListener<RealmResults<Event>>() {
            @Override
            public void onChange(final RealmResults<Event> element) {
                onThisDayRecycler.post(new Runnable() {
                    @Override
                    public void run() {
                        RealmList<Event> onThisDay = new RealmList<Event>();
                        for (int i = 0; i < element.size(); i++) {
                            Date mDate = element.get(i).getDatetime();
                            cal.setTime(mDate);
                            int mMonth = cal.get(Calendar.MONTH);
                            int mDay = cal.get(Calendar.DAY_OF_MONTH);
                            if ((mMonth == month) && (mDay == day)) {
                                onThisDay.add(element.get(i));
                            }
                        }
                        adapter.events = onThisDay;
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });


        return view;

    }
}
