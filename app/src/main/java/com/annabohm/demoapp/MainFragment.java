package com.annabohm.demoapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class MainFragment extends Fragment {
    NavController navController;
    Button addImageButton;
    Button galleryButton;
    ImageView chosenImage;
    StorageReference storageReference;
    DatabaseReference mDatabase;

    public Uri imageUri;

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
        addImageButton = view.findViewById(R.id.chooseImageButton);
        galleryButton = view.findViewById(R.id.toGalleryButton);
        galleryButton.setOnClickListener(galleryOnClickListener);

        storageReference = FirebaseStorage.getInstance().getReference("Images");
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://galeria-72a00-default-rtdb.firebaseio.com/");
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });
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

    private void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        //
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        //
        startActivityForResult(intent, 1);
    }

    private String getExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RotateBitmap rotateBitmap = new RotateBitmap();
        Bitmap bitmap = null;
        if (requestCode == 1 && resultCode == RESULT_OK) {

            if (data.getClipData() != null) {

                int totalItem = data.getClipData().getItemCount();

                for (int i = 0; i < totalItem; i++) {

                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
//                    String path = data.getClipData().getItemAt(i);
                    try {
                        bitmap = rotateBitmap.HandleSamplingAndRotationBitmap(getActivity(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //StorageReference mRef = storageReference.child("image").child(imagename);
                    final StorageReference mRef = storageReference.child(System.currentTimeMillis()+"."+getExtension(imageUri));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] byteData = baos.toByteArray();
                    mRef.putBytes(byteData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                            //
                            mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //You will get download URL in uri
                                    Log.d(TAG, "Download URL = "+ uri.toString());
                                    //Adding that URL to Realtime database
                                    mDatabase.child(String.valueOf(System.currentTimeMillis())).setValue(uri.toString());
                                }
                            });
                            //
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Process failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();

                //StorageReference mRef = storageReference.child("image").child(imagename);
                final StorageReference mRef = storageReference.child(System.currentTimeMillis()+"."+getExtension(imageUri));
                try {
                    bitmap = rotateBitmap.HandleSamplingAndRotationBitmap(getActivity(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] byteData = baos.toByteArray();
                mRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Single image uploaded successfully", Toast.LENGTH_SHORT).show();
                        //
                        mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //You will get download URL in uri
                                Log.d(TAG, "Download URL = "+ uri.toString());
                                //Adding that URL to Realtime database
                                mDatabase.child(String.valueOf(System.currentTimeMillis())).setValue(uri.toString());
                            }
                        });
                        //
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Process failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        //
    }
}