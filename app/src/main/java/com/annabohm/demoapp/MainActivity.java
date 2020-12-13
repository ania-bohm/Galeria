package com.annabohm.demoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity {
    NavController navController = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


}