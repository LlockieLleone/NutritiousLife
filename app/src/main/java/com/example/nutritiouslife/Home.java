package com.example.nutritiouslife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Home extends AppCompatActivity implements View.OnClickListener{
    Button btnLogout, btnCalculator;
    String userId = "id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initialize();
    }

    private void initialize() {
        btnLogout = findViewById(R.id.btnLogout);
        btnCalculator = findViewById(R.id.btnCalculator);
        userId = getIntent().getStringExtra("schedule");
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
        Toast.makeText(this, "Logout successfully", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void calculator() {
        Intent intent = new Intent(this, Calculator.class);
        intent.putExtra("id", userId);
        startActivity(intent);
    }
}