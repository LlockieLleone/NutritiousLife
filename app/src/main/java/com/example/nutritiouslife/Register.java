package com.example.nutritiouslife;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import model.Food;
import model.RecipeAdapter;
import model.User;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.UUID;

public class Register extends AppCompatActivity implements View.OnClickListener, ValueEventListener, ChildEventListener {

    EditText editTextId,editTextEmail,editTextFirstName,editTextLastName,editTextAge,editTextWeight,editTextHeight,editTextPassword,editTextRePassword;
    Button btnRegister;

    DatabaseReference userDatabase, userChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();

    }

    private void initialize() {

        editTextId = findViewById(R.id.editTextId);
        editTextAge = findViewById(R.id.editTextAge);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextHeight= findViewById(R.id.editTextHeight);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRePassword = findViewById(R.id.editTextRePassword);

        btnRegister = findViewById(R.id.btnRegister);

        userDatabase = FirebaseDatabase.getInstance().getReference("user");

        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id){
            case R.id.btnRegister:register(); break;
        }

    }

    private void register() {

        try {

            int id = Integer.valueOf(editTextId.getText().toString());
            String email = editTextEmail.getText().toString();
            String firstname = editTextFirstName.getText().toString();
            String lastname = editTextLastName.getText().toString();
            int age = Integer.valueOf(editTextAge.getText().toString());
            int weight = Integer.valueOf(editTextWeight.getText().toString());
            int height = Integer.valueOf(editTextHeight.getText().toString());
            String password = editTextPassword.getText().toString();
            String repassword = editTextRePassword.getText().toString();

            User user = new User(id,email,firstname,lastname,age,weight,height,password);

            if (password.equals(repassword)){

                userDatabase.child(String.valueOf(id)).setValue(user);
                Toast.makeText(this,
                        "One document is added successfully to the collection person : id " + id,
                        Toast.LENGTH_LONG).show();

                editTextId.setText(null);
                editTextEmail.setText(null);
                editTextFirstName.setText(null);
                editTextLastName.setText(null);
                editTextAge.setText(null);
                editTextHeight.setText(null);
                editTextWeight.setText(null);
                editTextPassword.setText(null);
                editTextRePassword.setText(null);

                Intent intent = new Intent(this, Home.class);
                startActivity(intent);

            }else{
                Toast.makeText(this,
                        "Password is not same, please confirm it ",
                        Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){
            Toast.makeText(this,
                    e.getMessage(),
                    Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
