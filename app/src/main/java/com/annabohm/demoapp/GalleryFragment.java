package com.annabohm.demoapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment{
    RecyclerView photoGrid;
    List<String> photos;
    Adapter adapter;
    DatabaseReference mReference;
    TextView photoCount;
    CardView cardView;
    boolean check_ScrollingUp;

    public GalleryFragment() {
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
        mReference = FirebaseDatabase.getInstance().getReference();

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                photos.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    photos.add(child.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        photoCount = view.findViewById(R.id.imageCountTextView);
        cardView = view.findViewById(R.id.cardView);
        check_ScrollingUp = false;
        adapter = new Adapter(getContext(), photos, photoCount);
        adapter.notifyDataSetChanged();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3, GridLayoutManager.VERTICAL, false);
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
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }
}