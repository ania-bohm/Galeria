package com.annabohm.demoapp;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Photo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Photo extends Fragment {

    ImageView photo;
    Bitmap bitmap;
    NavController navController;
    public Photo() {
        // Required empty public constructor
    }


    public static Photo newInstance(String param1, String param2) {
        Photo fragment = new Photo();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        photo = view.findViewById(R.id.bigImage);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });

        Bundle bundle = this.getArguments();
        Bitmap bitmap = bundle.getParcelable("photo");
        photo.setImageBitmap(bitmap);
    }
}