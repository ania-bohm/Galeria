package com.annabohm.demoapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    NavController navController;
    Button addImageButton;
    Button galleryButton;

    public MainFragment() {
        // Required empty public constructor
    }

    private View.OnClickListener addImageOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            navController.navigate(R.id.mainToChoose);
        }
    };
    private View.OnClickListener galleryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            navController.navigate(R.id.mainToGallery);
        }
    };

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        addImageButton = view.findViewById(R.id.addImageButton);
        galleryButton = view.findViewById(R.id.toGalleryButton);
        addImageButton.setOnClickListener(addImageOnClickListener);
        galleryButton.setOnClickListener(galleryOnClickListener);
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