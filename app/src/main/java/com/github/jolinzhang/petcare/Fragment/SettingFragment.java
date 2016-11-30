package com.github.jolinzhang.petcare.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.jolinzhang.petcare.MyPetsActivity;
import com.github.jolinzhang.petcare.R;

/**
 * Created by Jonelezhang on 11/24/16.
 */

public class SettingFragment extends Fragment {

    private TextView myPets;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);
        //change fragment title
        getActivity().setTitle("Setting");

        myPets = (TextView) view.findViewById(R.id.myPets);
        myPets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyPetsActivity.class);
                startActivity(intent);
            }
        });





        return view;
    }
}
