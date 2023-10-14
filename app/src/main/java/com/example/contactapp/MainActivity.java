package com.example.contactapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    //view
    private FloatingActionButton fab;
    private RecyclerView contactRv;

    private LinearLayout moveBackButton;

    //db
    private DbHelper dbHelper;

    //adapter
    private AdapterContact adapterContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);
        if (!isTableExists(dbHelper.getReadableDatabase(), Constants.TABLE_NAME))
        {
            dbHelper.getWritableDatabase().execSQL(Constants.CREATE_TABLE);
        }

        initializeCard();

        if (getIntent().hasExtra("exit")) {
            finish();
        }

        //initialization
        fab = findViewById(R.id.fab);
        contactRv = findViewById(R.id.contactRv);
        moveBackButton = findViewById(R.id.moveBackBtn);
        contactRv.setHasFixedSize(true);

        // add listener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move to new activity to add contact
                Intent intent = new Intent(MainActivity.this,AddEditContact.class);
                intent.putExtra("isEditMode",false);
                startActivity(intent);
                finish();
            }
        });

        moveBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });

        EditText search = findViewById(R.id.searchContact);
        FrameLayout card = findViewById(R.id.card);
        TextView cc = findViewById(R.id.cc);
        cc.setVisibility(View.GONE);
        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    // Hide the element when the EditText gains focus
                    cc.setVisibility(View.VISIBLE);
                    card.setVisibility(View.GONE);
                } else
                {
                    cc.setVisibility(View.GONE);
                    card.setVisibility(View.VISIBLE);
                }
            }
        });

        cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear the text inside the search EditText
                search.setText("");

                // Remove focus from the search EditText
                search.clearFocus();
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchContact(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        loadData();
    }

    private void initializeCard()
    {
        DbHelper2 dbHelper = new DbHelper2(this);
        if (!isTableExists(dbHelper.getReadableDatabase(), Constants.TABLE_NAME_2))
        {
            dbHelper.getWritableDatabase().execSQL(Constants.CREATE_TABLE_2);
        }

        if (dbHelper.getAllData().size() == 0)
        {
            String timeStamp = ""+System.currentTimeMillis();
            dbHelper.insertContact(
                    "" + R.drawable.ic_baseline_person_24,
                    "" + "Anh",
                    "" + "Tran",
                    "" + "HUST",
                    "" + "0973851011",
                    "" + "hnhtr1999@gmail.com",
                    "" + timeStamp,
                    "" + timeStamp
            );
        }

        ModelContact card = dbHelper.getAllData().get(0);

        ImageView imageView = findViewById(R.id.image);
        TextView fullName = findViewById(R.id.name);
        String image = card.getImage();

        if (image.equals("")){
            imageView.setImageResource(R.drawable.ic_baseline_person_24);
        }else {
            imageView.setImageResource(R.drawable.ic_baseline_person_24);
        }
        fullName.setText("Your card");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ContactDetails2.class);
                intent.putExtra("contactId", card.getId());
                startActivity(intent);
                finish();
            }
        });

        fullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ContactDetails2.class);
                intent.putExtra("contactId", card.getId());
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadData() {
        adapterContact = new AdapterContact(this,dbHelper.getAllData());
        contactRv.setAdapter(adapterContact);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(); // refresh data
    }

    private void searchContact(String query) {
        adapterContact = new AdapterContact(this,dbHelper.getSearchContact(query));
        contactRv.setAdapter(adapterContact);
    }

    public boolean isTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        try {
            if (cursor != null && cursor.moveToFirst()) {
                // The table exists
                return true;
            } else {
                // The table doesn't exist
                return false;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}