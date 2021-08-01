package com.example.vinayaksapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Details extends AppCompatActivity {
    TextView name1;
    TextView emailId1;
    TextView phNo1;
    TextView age1;
    TextView gender1;
    ImageView photo1;
    VideoView video1;
    MediaController mediaController;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        name1 = findViewById(R.id.name);
        emailId1 = findViewById(R.id.email);
        phNo1 = findViewById(R.id.phoneNo);
        age1 = findViewById(R.id.age);
        gender1 = findViewById(R.id.gender);
        photo1 = findViewById(R.id.image);
        video1 = findViewById(R.id.video_view);
        mediaController = new MediaController(this);
        storage = FirebaseStorage.getInstance();


        String name = getIntent().getStringExtra("name");
        String age = getIntent().getStringExtra("age");
        String phNo = getIntent().getStringExtra("phoneNo");
        String gender = getIntent().getStringExtra("gender");
        String emailId = getIntent().getStringExtra("emailId");
        String imageUri = getIntent().getStringExtra("imageUri");
        String videoUri = getIntent().getStringExtra("videoUri");
        video1.setMediaController(mediaController);
        mediaController.setAnchorView(video1);
        video1.start();


        name1.setText("NAME: " + name);
        age1.setText("AGE: " + age);
        phNo1.setText("Phone No: " + phNo);
        gender1.setText("Gender: " + gender);
        emailId1.setText("Email ID: " + emailId);

        Uri myUriimage = Uri.parse(imageUri);
        Glide.with(Details.this).load(myUriimage).into(photo1);
        final File localFile;
        try {
            localFile = File.createTempFile("userIntro", "3gp");
            StorageReference reference = storage.getReference().child("Videos");

            reference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                video1.setVideoPath(videoUri);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}