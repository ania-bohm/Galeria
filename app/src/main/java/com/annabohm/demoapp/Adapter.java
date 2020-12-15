package com.annabohm.demoapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    List<String> photos;
    LayoutInflater inflater;
    DatabaseReference mReference;
    Context context;
    NavController navController;

    public Adapter(Context context, List<String> photos) {
        this.photos = photos;
        this.inflater = LayoutInflater.from(context);
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mini_photo, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Picasso.get().load(photos.get(position)).resize(400,400).centerCrop().into(holder.photo);
//        Picasso.get().load(photos.get(position)).resize(400,400).centerCrop().into(holder.bigPhoto);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);

            photo.setOnClickListener(this);

        }

        @Override
        public void onClick(final View view) {
            Bundle bundle = new Bundle();
            final String[] toast_message = new String[1];
            final int position = this.getLayoutPosition();
            navController = Navigation.findNavController(view);
            BitmapDrawable drawable = (BitmapDrawable) photo.getDrawable();
            Bitmap bitmap = drawable. getBitmap();
            bundle.putParcelable("photo",bitmap);
            navController.navigate(R.id.galleryToPhoto, bundle);
        }
    }
}
