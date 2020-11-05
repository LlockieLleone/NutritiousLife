package com.example.nutritiouslife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity implements View.OnClickListener, ValueEventListener {
    Button btnLogout, btnCalculator;
    String userId = "";
    private int weight;

    DatabaseReference userDatabase, userChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initialize();
    }

    private void initialize() {
        btnLogout = findViewById(R.id.btnLogout);
        btnCalculator = findViewById(R.id.btnCalculator);
        userId = getIntent().getStringExtra("id");

        userDatabase = FirebaseDatabase.getInstance().getReference("user");

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id) {
            case R.id.btnLogout: logout();break;
            case R.id.btnCalculator: calculator();break;
        }
    }

    private void logout() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void calculator() {
        userChild = FirebaseDatabase.getInstance().getReference("user").child(String.valueOf(userId));

        userChild.addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists()) {

            weight = Integer.valueOf(dataSnapshot.child("weight").getValue().toString());

            Intent intent = new Intent(this, Calculator.class);
            intent.putExtra("id", userId);
            intent.putExtra("80", weight);

            Toast.makeText(this,
                    "data before send is " + weight,
                    Toast.LENGTH_LONG).show();

            startActivity(intent);
        }else{
            Toast.makeText(this,
                    "Fail to find user data from database",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}