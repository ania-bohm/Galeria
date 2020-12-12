package com.annabohm.demoapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    RecyclerView photoGrid;
    List<Integer> photos;
    Adapter adapter;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        photoGrid = view.findViewById(R.id.rv);
        photos = new ArrayList<>();
        photos.add(R.drawable.cat1);
        photos.add(R.drawable.cat2);
        photos.add(R.drawable.cat1);
        photos.add(R.drawable.cat2);
        photos.add(R.drawable.cat2);
        photos.add(R.drawable.cat2);
        photos.add(R.drawable.cat1);
        photos.add(R.drawable.cat2);
        photos.add(R.drawable.cat1);
        photos.add(R.drawable.cat2);
        photos.add(R.drawable.cat1);
        photos.add(R.drawable.cat2);

        adapter = new Adapter(getContext(), photos);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2, GridLayoutManager.VERTICAL, false);
        photoGrid.setLayoutManager(layoutManager);
        photoGrid.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


}