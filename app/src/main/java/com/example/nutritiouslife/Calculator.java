package com.example.nutritiouslife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.User;

public class Calculator extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    private EditText editTextWeightForCalculate;
    private RadioGroup rgIntensity, rgNeed;
    private RadioButton rbHigh, rbMedium, rbLow, rbLose, rbGain, rbMaintain;
    private TextView textViewWaterResult, textViewCaloriesResult;
    private Button btnCalculate;

    private User aUser;

    private String intensity = "";
    private String need = "";

    private int waterVolume;
    private int caloriesAmount;

    private String userId = "";

    private int height;
    private int age;
    private int weight;
    private String weightFromHome;

    DatabaseReference userDatabase, userChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        initialize();
    }

    private void initialize() {
        editTextWeightForCalculate = findViewById(R.id.editTextWeightForCalculate);
        rgIntensity = findViewById(R.id.rgIntensity);
        rgNeed = findViewById(R.id.rgNeed);
        rbHigh = findViewById(R.id.rbHigh);
        rbMedium = findViewById(R.id.rbMedium);
        rbLow = findViewById(R.id.rbLow);
        rbGain = findViewById(R.id.rbGain);
        rbMaintain = findViewById(R.id.rbMaintain);
        rbLose = findViewById(R.id.rbLose);
        textViewWaterResult = findViewById(R.id.textViewWaterResult);
        textViewCaloriesResult = findViewById(R.id.textViewCaloriesResult);
        btnCalculate = findViewById(R.id.btnCalculate);

        rbLose.setOnClickListener(this);
        rbMaintain.setOnClickListener(this);
        rbGain.setOnClickListener(this);
        rbLow.setOnClickListener(this);
        rbMedium.setOnClickListener(this);
        rbHigh.setOnClickListener(this);
        btnCalculate.setOnClickListener(this);

        userId = getIntent().getStringExtra("id");
        weightFromHome = getIntent().getStringExtra("weight");

        Toast.makeText(this,
                "data get from home is " + weightFromHome,
                Toast.LENGTH_LONG).show();

        editTextWeightForCalculate.setText(String.valueOf(weight));

        userDatabase = FirebaseDatabase.getInstance().getReference("user");

        userChild = FirebaseDatabase.getInstance().getReference("user").child(String.valueOf(userId));

        userChild.addValueEventListener(this);


    }

    private int calculateWaterVolume(String intensity) throws Exception{
        int volume = 0;
        if(editTextWeightForCalculate.getText().toString() != "") {
            weight = Integer.valueOf(editTextWeightForCalculate.getText().toString());

            if(intensity.equals("High")) {
                volume = (weight * 50) - 1500;
                //need to consider the calories intake(food),
                //to change the water taken from food and self produce(current 1500)
            }else if(intensity.equals("Medium")) {
                volume = (weight * 45) - 1500;
            }else if(intensity.equals("Low")) {
                volume = (weight * 40) - 1500;
            }else {
                throw new Exception("Please select intensity of your activity");
            }
        }else{
            throw new Exception("Please enter your weight");
        }

        return volume;
    }

    private int calculateCalories(String need) throws Exception{
        int cal = 0;
        weight = Integer.valueOf(editTextWeightForCalculate.getText().toString());
        if(editTextWeightForCalculate.getText().toString() != "") {

            if (need.equals("Lose")) {
                cal = (int)((67 + (13.73 * weight) + (5 * height) - (6.9 * age)) * 1.1);
            } else if (need.equals("Gain")) {
                cal = (int)((67 + (13.73 * weight) + (5 * height) - (6.9 * age)) * 1.3);
            } else if (need.equals("Maintain")) {
                cal = (int)((67 + (13.73 * weight) + (5 * height) - (6.9 * age)) * 1.2);
            } else {
                throw new Exception("Please select your need of weight control");
            }

//            if(gender.equals("male")){
//
//            } else if(gender.equals("female")){
//
//            } else if(gender.equals("other")){
//
//            }
        }
        return cal;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists()) {

            height = Integer.valueOf(dataSnapshot.child("height").getValue().toString());
            age =  Integer.valueOf(dataSnapshot.child("age").getValue().toString());

        }else{
            Toast.makeText(this,
                    "Fail to find data from database",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void showWaterResult() {
        try
        {
            waterVolume = calculateWaterVolume(intensity);
            textViewWaterResult.setText(waterVolume + "ml water");
        }
        catch(Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void showCaloriesResult() {
        try
        {
            caloriesAmount = calculateCalories(need);
            textViewCaloriesResult.setText(caloriesAmount + "cal food");
        }
        catch(Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.rbHigh:
                intensity = rbHigh.getText().toString();
                break;
            case R.id.rbMedium:
                intensity = rbMedium.getText().toString();
                break;
            case R.id.rbLow:
                intensity = rbLow.getText().toString();
                break;
            case R.id.rbGain:
                need = rbGain.getText().toString();
                break;
            case R.id.rbMaintain:
                need = rbMaintain.getText().toString();
                break;
            case R.id.rbLose:
                need = rbLose.getText().toString();
                break;
            case R.id.btnCalculate:
                showWaterResult();
                showCaloriesResult();
                break;
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}