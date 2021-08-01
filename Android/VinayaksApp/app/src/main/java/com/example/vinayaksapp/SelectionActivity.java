package com.example.vinayaksapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectionActivity extends AppCompatActivity {

    Button adminbutton;
    Button usersbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        adminbutton = findViewById(R.id.adminbutton);
        usersbutton = findViewById(R.id.usersbutton);


        adminbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectionActivity.this, Password.class);
                startActivity(intent);
                finish();
            }
        });

        usersbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectionActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}