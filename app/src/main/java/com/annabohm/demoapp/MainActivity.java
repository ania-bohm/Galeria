package com.annabohm.demoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    NavController navController = null;
    MenuItem actionCancel;
    MenuItem actionDelete;
    Menu mMenu = null;
    static MainActivity mThis = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mThis = this;
//        deleteItem = findViewById(R.id.action_cancel);
//        cancelItem = findViewById(R.id.action_delete);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        actionCancel = findViewById(R.id.action_cancel);
        actionDelete = findViewById(R.id.action_delete);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_delete:
              //  addSomething();
                return true;
            case R.id.action_cancel:
              //  startSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public boolean onPrepareOptionsMenu(Menu menu) {
        mMenu = menu;
        return super.onPrepareOptionsMenu(menu);
    }


}