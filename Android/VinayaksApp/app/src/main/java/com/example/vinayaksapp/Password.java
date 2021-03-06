package com.example.vinayaksapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Password extends AppCompatActivity {

    EditText password;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        password = findViewById(R.id.editTextNumberPassword);
        submit = findViewById(R.id.checkbutton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = password.getText().toString();
                if (pass.equals("123")) {
                    Intent intent = new Intent(Password.this, Admin.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Password.this, "Password not correct", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Password.this, SelectionActivity.class);
        startActivity(intent);
        finishAffinity();


    }


}