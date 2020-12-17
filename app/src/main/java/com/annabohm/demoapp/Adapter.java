package com.annabohm.demoapp;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    List<String> photos;
    LayoutInflater inflater;
    DatabaseReference mReference;
    StorageReference photoRef;
    Context context;
    NavController navController;
    boolean checkBoxVisible;
    int imageSelected;
    TextView photoCount;
    MenuItem actionDelete;
    MenuItem actionCancel;
    Menu menu;
    ArrayList <Integer> selectedList;
    RecyclerView rv;


    public Adapter(Context context, List<String> photos, TextView photoCount) {
        this.photos = photos;
        this.inflater = LayoutInflater.from(context);
        this.context=context;
        this.photoCount = photoCount;
        checkBoxVisible = false;
        imageSelected = 0;
        menu = MainActivity.mThis.mMenu;
        actionCancel = menu.findItem(R.id.action_cancel);
        actionDelete = menu.findItem(R.id.action_delete);
        selectedList = new ArrayList<>();
        actionDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                deleteImages();
                actionDelete.setVisible(false);
                actionCancel.setVisible(false);
                return true;
            }
        });
        actionCancel.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                cancelSelection();
                actionDelete.setVisible(false);
                actionCancel.setVisible(false);
                return true;
            }
        });

    }
    public void deleteImages() {

        for (int position:selectedList) {
            String uri = photos.get(position);

            photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(uri);
            mReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://galeria-72a00-default-rtdb.firebaseio.com/");
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds: snapshot.getChildren()){
//                        Toast.makeText(context, ds.getValue()+" === "+ uri, Toast.LENGTH_SHORT).show();
                        if(uri.equals(ds.getValue())) {
                            mReference.child(ds.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: deleted from database");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: did not delete file from database");
                                }
                            });
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                    Log.d(TAG, "onSuccess: deleted from storage");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                    Log.d(TAG, "onFailure: did not delete file from storage");
                }
            });
        }
        clearEVERYTHING();
        notifyDataSetChanged();
    }

    public void cancelSelection() {
        clearEVERYTHING();
        notifyDataSetChanged();
    }

    public void clearEVERYTHING(){
        imageSelected = 0;
        checkBoxVisible = false;
//        photos.clear();
        selectedList.clear();
    }


    public void deactivateCheckboxes(){
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mini_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Picasso.get().load(photos.get(holder.getAdapterPosition())).resize(400,400).centerCrop().into(holder.photo);
        holder.deleteCheckBox.setChecked(false);
        photoCount.setText(""+this.getItemCount()+" Photos");
        if(checkBoxVisible){
            holder.deleteCheckBox.setVisibility(View.VISIBLE);
        }
        else{
            holder.deleteCheckBox.setVisibility(View.GONE);
        }

//        Picasso.get().load(photos.get(position)).resize(400,400).centerCrop().into(holder.bigPhoto);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder  {
        ImageView photo;
        CheckBox deleteCheckBox;
        boolean isChecked;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            actionDelete = ((MainActivity) context).findViewById(R.id.action_delete);
//            actionCancel = ((MainActivity) context).findViewById(R.id.action_cancel);
            isChecked = false;
            photo = itemView.findViewById(R.id.photo);
            deleteCheckBox = itemView.findViewById(R.id.deleteCheckBox);
            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    navController = Navigation.findNavController(view);
                    BitmapDrawable drawable = (BitmapDrawable) photo.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    bundle.putParcelable("photo",bitmap);
                    navController.navigate(R.id.galleryToPhoto, bundle);
                }
            });
            photo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    checkBoxVisible = true;
                    notifyDataSetChanged();
                    return true;
                }
            });
            deleteCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   if(deleteCheckBox.isChecked()){
                       imageSelected++;
                       selectedList.add(getAdapterPosition());
                   } else{
                       imageSelected--;
                       selectedList.remove(selectedList.indexOf(getAdapterPosition()));
                   }
                   if(imageSelected>0){
                       actionCancel.setVisible(true);
                       actionDelete.setVisible(true);
                   }
                   else{
                       actionCancel.setVisible(false);
                       actionDelete.setVisible(false);
                   }
                }
            });

        }

    }
}
