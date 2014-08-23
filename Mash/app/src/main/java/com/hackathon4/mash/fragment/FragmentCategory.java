package com.hackathon4.mash.fragment;

/**
 * Created by stephenquick on 8/23/14.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hackathon4.mash.R;

public class FragmentCategory extends Fragment {

    private static final String TAG = FragmentCategory.class.getName();
//test
    public FragmentCategory() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView()");

        return inflater.inflate(R.layout.fragment_category, container, false);
    }
}
