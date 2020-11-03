package com.example.nutritiouslife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    EditText editTextId, editTextPassword;
    Button btnLogin;
    DatabaseReference userDatabase, userChild;
    TextView textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize(){
        editTextId = findViewById(R.id.editTextId);
        editTextPassword = findViewById(R.id.editTextPassword);

        btnLogin = findViewById(R.id.btnLogin);

        textViewRegister= findViewById(R.id.textViewRegister);

        userDatabase = FirebaseDatabase.getInstance().getReference("user");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id) {
            case R.id.btnLogin:
                login();
                break;
            case R.id.textViewRegister:
                register();
                break;
        }
    }

    private void register() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    private void login(){
        String id = editTextId.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        userChild = FirebaseDatabase.getInstance().getReference("user").child(String.valueOf(id));
        if (TextUtils.isEmpty(id)&&TextUtils.isEmpty(password)) {
            editTextId.setError("Email and Password required");
            return;
        }
        userChild.addValueEventListener(this);
    }


    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()){
            Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

}