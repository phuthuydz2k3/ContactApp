package com.example.contactapp;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Permission;

public class AddEditContact extends AppCompatActivity {

    private ImageView profileIv;
    private EditText fNameEt, lNameEt, cNameEt, pNumEt, emailEt;
    private TextView cancelTv, saveTv, addPicTv;

    private String id, image, fName, lName, cName, pNum, email, addedTime, updatedTime;
    private Boolean isEditMode;

    //permission constant
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 200;
    private static final int IMAGE_FROM_GALLERY_CODE = 300;
    private static final int IMAGE_FROM_CAMERA_CODE = 400;

    // string array of permission
    private String[] cameraPermission;
    private String[] storagePermission;

    //Image uri var
    private Uri imageUri;

    //database helper
    private DbHelper dbHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);

        //init db
        dbHelper = new DbHelper(this);

        //init permission
        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};



        //init view
        profileIv = findViewById(R.id.profileIv);
        fNameEt = findViewById(R.id.fNameEt);
        lNameEt = findViewById(R.id.lNameEt);
        cNameEt = findViewById(R.id.cNameEt);
        pNumEt = findViewById(R.id.pNumEt);
        emailEt = findViewById(R.id.emailEt);
        cancelTv = findViewById(R.id.cancelTv);
        saveTv = findViewById(R.id.saveTv);
        addPicTv = findViewById(R.id.addPicTv);

        // get intent data
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode",false);

        if (isEditMode){
            //set toolbar title

            //get the other value from intent
            id = intent.getStringExtra("ID");
            fName = intent.getStringExtra("FNAME");
            lName = intent.getStringExtra("LNAME");
            pNum = intent.getStringExtra("P_NUM");
            email = intent.getStringExtra("EMAIL");
            cName = intent.getStringExtra("CNAME");
            addedTime = intent.getStringExtra("ADDEDTIME");
            updatedTime = intent.getStringExtra("UPDATEDTIME");
            image = intent.getStringExtra("IMAGE");
            TextView action = findViewById(R.id.action);
            action.setText("Edit contact");

            //set value in editText field
            fNameEt.setText(fName);
            lNameEt.setText(lName);
            pNumEt.setText(pNum);
            emailEt.setText(email);
            cNameEt.setText(cName);
            imageUri = Uri.parse(image);

            if (image.equals("") || image.equals("null")){
                profileIv.setImageResource(R.drawable.ic_baseline_person_24);
            }else {
                profileIv.setImageURI(imageUri);
            }
        }else {
            // add mode on
        }

        // add even handler
        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickerDialog();
            }
        });

        addPicTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickerDialog();
            }
        });

        saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backHome();
            }
        });
    }

    private void showImagePickerDialog() {

        //option for dialog
        String options[] = {"Camera","Gallery"};

        // Alert dialog builder
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);

        //setTitle
        builder.setTitle("Choose An Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle item click
                if (which == 0){ //start from 0 index
                    //camera selected
                    if (!checkCameraPermission()){
                        //request camera permission
                        requestCameraPermission();
                    }else {
                        pickFromCamera();
                    }
                        
                }else if (which == 1){
                    //Gallery selected
                    if (!checkStoragePermission()){
                        //request storage permission
                        requestStoragePermission();
                    }else {
                        pickFromGallery();
                    }
                    
                }
            }
        }).create().show();
    }

    private void pickFromGallery() {
        //intent for taking image from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*"); // only Image

        startActivityForResult(galleryIntent,IMAGE_FROM_GALLERY_CODE);
    }

    private void pickFromCamera() {

//       ContentValues for image info
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"IMAGE_TITLE");
        values.put(MediaStore.Images.Media.DESCRIPTION,"IMAGE_DETAIL");

        //save imageUri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //intent to open camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);

        startActivityForResult(cameraIntent,IMAGE_FROM_CAMERA_CODE);
    }

    private void saveData() {

        //take user giver data in variable
        fName = fNameEt.getText().toString();
        lName = lNameEt.getText().toString();
        cName = cNameEt.getText().toString();
        pNum = pNumEt.getText().toString();
        email = emailEt.getText().toString();

        // get current time to save as added time
        String timeStamp = ""+System.currentTimeMillis();


        //check filed data
        if (!pNum.isEmpty()) {
            if (isEditMode){
                 dbHelper.updateContact(
                        "" + id,
                        "" + imageUri.toString(),
                        "" + fName,
                        "" + lName,
                        "" + cName,
                        "" + pNum,
                        "" + email,
                        "" + addedTime,
                         "" + timeStamp // updated time will new time
                );

                Toast.makeText(getApplicationContext(), "Updated Successfully....", Toast.LENGTH_SHORT).show();

            }else {
                // add mode
                long id =  dbHelper.insertContact(
                        "" + imageUri,
                        "" + fName,
                        "" + lName,
                        "" + cName,
                        "" + pNum,
                        "" + email,
                        "" + timeStamp,
                        "" + timeStamp
                );
                //To check insert data successfully ,show a toast message
                Toast.makeText(getApplicationContext(), "Inserted Successfully.... "+id, Toast.LENGTH_SHORT).show();
            }

        }else {
            // show toast message
            Toast.makeText(getApplicationContext(), "Nothing to save....", Toast.LENGTH_SHORT).show();
        }

        backHome();

    }

    private void backHome() {
        Intent intent;
        intent = new Intent(AddEditContact.this, MainActivity.class);
        if (isEditMode)
        {
            intent.putExtra("exit", "exit");
        }
        startActivity(intent);
        finish();
    }

    //ctr + O

    //back button click
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    //check camera permission
    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result & result1;
    }

    //request for camera permission
    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_PERMISSION_CODE); // handle request permission on override method
    }

    //check storage permission
    private boolean checkStoragePermission(){
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result1;
    }

    //request for camera permission
    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_PERMISSION_CODE);
    }




    //handle request permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_PERMISSION_CODE:
                if (grantResults.length >0){

                    //if all permission allowed return true , otherwise false
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted){
                        //both permission granted
                        pickFromCamera();
                    }else {
                        //permission not granted
                        Toast.makeText(getApplicationContext(), "Camera & Storage Permission needed..", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case STORAGE_PERMISSION_CODE:
                if (grantResults.length >0){

                    //if all permission allowed return true , otherwise false
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted){
                        //permission granted
                        pickFromGallery();
                    }else {
                        //permission not granted
                        Toast.makeText(getApplicationContext(), "Storage Permission needed..", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_FROM_GALLERY_CODE){
                // picked image from gallery
                //crop image
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(AddEditContact.this);

            }else if (requestCode == IMAGE_FROM_CAMERA_CODE){
                //picked image from camera
                //crop Image
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(AddEditContact.this);
            }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                //cropped image received
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageUri = result.getUri();
                profileIv.setImageURI(imageUri);

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //for error handling
                Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
}