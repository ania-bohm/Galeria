package com.annabohm.demoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public int randomNumber;

    public static int generateRandomNumber(int min, int max) {
        return (min + (int) (Math.random() * ((max - min) + 1)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        randomNumber = generateRandomNumber(0, 20);
    }

//    public void changeImageView(View view) {
//        ImageView image = (ImageView) findViewById(R.id.imageView);
//        image.setImageResource(R.drawable.cat2);
//        //image.setImageDrawable(getResources().getDrawable(R.drawable.cat2));
//    }
//
//    public void guessClicked(View view) {
//        EditText guessEditText = (EditText) findViewById(R.id.guessEditText);
//        int guess = Integer.parseInt(guessEditText.getText().toString());
//        if (guess == randomNumber) {
//            Toast.makeText(this, "That's right!", Toast.LENGTH_SHORT).show();
//            randomNumber = generateRandomNumber(0, 20);
//        } else if (guess > randomNumber) {
//            Toast.makeText(this, "Lower!", Toast.LENGTH_SHORT).show();
//        }
//        else if (guess < randomNumber) {
//            Toast.makeText(this, "Higher!", Toast.LENGTH_SHORT).show();
//        }
//    }
}