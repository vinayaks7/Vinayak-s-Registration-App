package com.example.vinayaksapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_VIDEO = 101;
    private EditText name;
    private EditText email;
    private EditText phNo;
    private EditText age;
    private EditText gender;
    private Uri imageUri;
    private Uri videoUri;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private String TAG = "fafa";
    private String videoUrltofirebase = "";
    private String imageUrltofirebase = "";
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    ProgressDialog dialog;



    Uri picUri;

    private static final int REQUEST_CAPTURE_VIDEO = 100;
    private static final int PICK_IMAGE_REQUEST = 101;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phNo = findViewById(R.id.phno);
        age = findViewById(R.id.age);
        gender = findViewById(R.id.gender);
        Button onSubmit = findViewById(R.id.onSubmit);
        Button addPhoto = findViewById(R.id.addphoto);
        Button addVideo = findViewById(R.id.addvideo);
        Button shootVideo = findViewById(R.id.shootvideo);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermission();


            }
        });

        shootVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturevideo();
            }
        });

        addVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takerecordedVideo();
            }
        });


        onSubmit.setOnClickListener(v -> {
            String name = MainActivity.this.name.getText().toString();
            String phNo = MainActivity.this.phNo.getText().toString();
            String email = MainActivity.this.email.getText().toString();
            String age = MainActivity.this.age.getText().toString();
            String gender = MainActivity.this.gender.getText().toString();

            if (name.isEmpty()) {
                MainActivity.this.name.setError("Please Enter the name");
                return;
            }

            if (phNo.isEmpty()) {
                MainActivity.this.phNo.setError("Please Enter the name");
                return;
            }
            if (email.isEmpty()) {
                MainActivity.this.email.setError("Please Enter the name");
                return;
            }
            if (age.isEmpty()) {
                MainActivity.this.age.setError("Please Enter the name");
                return;
            }
            if (gender.isEmpty()) {
                MainActivity.this.gender.setError("Please Enter the name");
                return;
            }

            if (imageUrltofirebase.isEmpty()) {
                Toast.makeText(this, "Capture a image", Toast.LENGTH_SHORT).show();
                return;
            }
            if (videoUrltofirebase.isEmpty()) {
                Toast.makeText(this, "Shoot a video and upload it", Toast.LENGTH_SHORT).show();
                return;
            }

            User obj1 = new User(name, phNo, email, age, gender, imageUrltofirebase, videoUrltofirebase);
            database.getReference().child("users").push().setValue(obj1).addOnSuccessListener(aVoid -> {
                MainActivity.this.name.getText().clear();
                MainActivity.this.phNo.getText().clear();
                MainActivity.this.age.getText().clear();
                MainActivity.this.gender.getText().clear();
                MainActivity.this.email.getText().clear();
                imageUrltofirebase = "";
                videoUrltofirebase = "";
            });


        });


    }

    private void askCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {

            dispatchTakePictureIntent();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void capturevideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivity(intent);
    }

    private void takerecordedVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                REQUEST_CAPTURE_VIDEO);
    }


    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        Log.d(TAG, "Inside currentPath");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File storageDir=getExternalCacheDir();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                Toast.makeText(this, "Reaching here100", Toast.LENGTH_SHORT).show();
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Reaching here", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.harry.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CAPTURE_VIDEO) {
            dialog=new ProgressDialog(this);
            dialog.setMessage("Uploading video");
            dialog.setCancelable(false);
            dialog.show();

            if (data != null) {
                if (data.getData() != null) {

                    Calendar calendar = Calendar.getInstance();

                    videoUri = data.getData();

                    if (videoUri != null) {
                        StorageReference reference = storage.getReference().child("Videos").child(calendar.getTimeInMillis() + "");
                        reference.putFile(videoUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            videoUrltofirebase = uri.toString();
                                            dialog.dismiss();

                                            Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(MainActivity.this, "Task failed", Toast.LENGTH_SHORT).show();
                                }


                            }

                        });
                    } else {
                        Toast.makeText(this, "FilePath is null", Toast.LENGTH_SHORT).show();
                    }


                }
            }


        } else if (requestCode == CAMERA_REQUEST_CODE) {

            dialog=new ProgressDialog(this);
            dialog.setMessage("Uploading Image");
            dialog.setCancelable(false);
            dialog.show();
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                picUri = Uri.fromFile(f);
                Log.d(TAG, Uri.fromFile(f).toString());

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                Calendar calendar = Calendar.getInstance();

                if (contentUri != null) {
                    StorageReference reference = storage.getReference().child("Images").child(calendar.getTimeInMillis() + "");
                    reference.putFile(contentUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageUrltofirebase = uri.toString();
                                        dialog.dismiss();
//                                    imageUrl=uri.toString();
//                                    database.getReference().child("Images").push().setValue(imageUrl);
                                        Log.d("fafa", uri.toString());
                                        Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            } else {
                                Toast.makeText(MainActivity.this, "Task failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(this, "FilePath is null", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, SelectionActivity.class);
        startActivity(intent);
        finishAffinity();


    }


}
