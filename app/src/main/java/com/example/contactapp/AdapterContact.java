package com.example.contactapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterContact extends RecyclerView.Adapter<AdapterContact.ContactViewHolder> {

    private Context context;
    private ArrayList<ModelContact> contactList;
    private DbHelper dbHelper;

    // add constructor
    // alt + ins

    public AdapterContact(Context context, ArrayList<ModelContact> contactList) {
        this.context = context;
        this.contactList = contactList;
        dbHelper = new DbHelper(context);
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_contact_item,parent,false);
        ContactViewHolder vh = new ContactViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ModelContact modelContact = contactList.get(position);

        //get data
        //we need only All data
        String id = modelContact.getId();
        String image = modelContact.getImage();
        String fName = modelContact.getfName();

        //set data in view
        holder.contactName.setText(fName);
        if (image.equals("") || image.equals("null")){
            holder.contactImage.setImageResource(R.drawable.ic_baseline_person_24);
        }else {
            holder.contactImage.setImageURI(Uri.parse(image));
        }

        //handle item click and show contact details
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create intent to move to contactsDetails Activity with contact id as reference
                Intent intent = new Intent(context,ContactDetails.class);
                intent.putExtra("contactId", id);
                intent.putExtra("contactPosition", String.valueOf(position));
                context.startActivity(intent);
            }
        });

        // handle delete click
        holder.contactDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // we need database helper class reference
                dbHelper.deleteContact(id);

                //refresh data by calling resume state of MainActivity
                ((MainActivity) context).onResume();

            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder{

        //view for row_contact_item
        ImageView contactImage, contactDelete;
        TextView contactName;
        RelativeLayout relativeLayout;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            //init view
            contactImage = itemView.findViewById(R.id.contact_image);
            contactName = itemView.findViewById(R.id.contact_name);
            contactDelete = itemView.findViewById(R.id.contact_delete);
            relativeLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
