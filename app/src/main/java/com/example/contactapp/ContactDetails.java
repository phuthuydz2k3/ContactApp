package com.example.contactapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContactDetails extends AppCompatActivity {
    private TextView fNameTv, lNameTv, pNumTv, emailTv, cNameTv, editTv, textTv, callingTv, videoTv, gmailTv;
    private LinearLayout exitTv;
    private ImageView profileIv;

    private String id;

    //database helper
    private DbHelper dbHelper;

    private void backHome()
    {
        Intent intent = new Intent(ContactDetails.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        //init db
        dbHelper = new DbHelper(this);

        //get data from intent
        Intent intent = getIntent();
        id = intent.getStringExtra("contactId");

        //init view
        fNameTv = findViewById(R.id.fNameTv);
        lNameTv = findViewById(R.id.fullNameTv);
        pNumTv = findViewById(R.id.pNumTv);
        emailTv = findViewById(R.id.emailTv);
        cNameTv = findViewById(R.id.cNameTv);
        profileIv = findViewById(R.id.profileIv);
        exitTv = findViewById(R.id.exitTv);
        editTv = findViewById(R.id.editTv);
        textTv = findViewById(R.id.text);
        callingTv = findViewById(R.id.calling);
        videoTv = findViewById(R.id.video);
        gmailTv = findViewById(R.id.gmail);

        textTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Hello!", Toast.LENGTH_SHORT).show();
            }
        });

        callingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Hello!", Toast.LENGTH_SHORT).show();
            }
        });

        videoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Hello!", Toast.LENGTH_SHORT).show();
            }
        });

        gmailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Hello!", Toast.LENGTH_SHORT).show();
            }
        });


        exitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backHome();
            }
        });

        editTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactDetails.this , AddEditContact.class);

                ModelContact modelContact = dbHelper.getAllData().get(0);
                intent.putExtra("ID", id);
                intent.putExtra("FNAME", modelContact.getfName());
                intent.putExtra("LNAME", modelContact.getlName());
                intent.putExtra("P_NUM", modelContact.getpNum());
                intent.putExtra("EMAIL", modelContact.getEmail());
                intent.putExtra("CNAME", modelContact.getcName());
                intent.putExtra("IMAGE", modelContact.getImage());

                // pass a boolean data to define it is for edit purpose
                intent.putExtra("isEditMode",true);

                //start intent
                startActivity(intent);
                finish();
            }
        });

        loadDataById();
    }

    private void loadDataById() {

        //get data from database
        //query for find data by id
        String selectQuery =  "SELECT * FROM "+Constants.TABLE_NAME + " WHERE " + Constants.C_ID + " =\"" + id + "\"";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){
            do {
                //get data
                String fName =  "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_FNAME));
                String lName =  "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_LNAME));
                String image = "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_IMAGE));
                String phone = "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_PNUM));
                String email = "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_EMAIL));
                String cName = "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_CNAME));

                fNameTv.setText(fName);
                lNameTv.setText(fName + " " + lName);
                pNumTv.setText(phone);
                emailTv.setText(email);
                cNameTv.setText(cName);

                if (image.equals("null"))
                {
                    profileIv.setImageResource(R.drawable.ic_baseline_person_24);
                }else {
                    profileIv.setImageURI(Uri.parse(image));
                }

            }while (cursor.moveToNext());
        }

        db.close();
    }
}