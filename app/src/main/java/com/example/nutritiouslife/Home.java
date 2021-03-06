package com.example.nutritiouslife;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Food;
import model.User;

import static android.widget.Toast.LENGTH_LONG;

public class Home extends AppCompatActivity implements View.OnClickListener{
    Button btnLogout, btnCalculator, btnAddSearch, btnAddManual;
    String userId = "";
    private int weight;
    EditText editTextSearch, editTextFoodNameByUser, editTextCaloriesByUser;
    TextView textViewBreakfast, textViewLunch, textViewDinner;
    RadioButton rbBreakfast, rbLunch, rbDinner;
    String selection = "";
    Context HomeContext = this;

    String TotalBreakfast = "", TotalLunch = "", TotalDinner = "";




    DatabaseReference userDatabase, userChild, foodDatabase, SpecificfoodChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initialize();
    }

    private void initialize() {
        btnLogout = findViewById(R.id.btnLogout);
        btnCalculator = findViewById(R.id.btnCalculator);
        btnAddSearch = findViewById(R.id.btnAddSearch);

        rbBreakfast = findViewById(R.id.rbBreakfast);
        rbLunch = findViewById(R.id.rbLunch);
        rbDinner = findViewById(R.id.rbDinner);

        editTextSearch = findViewById(R.id.editTextSearch);

        textViewBreakfast = findViewById(R.id.textViewBreakfast);
        textViewDinner = findViewById(R.id.textViewDinner);
        textViewLunch = findViewById(R.id.textViewLunch);

        userId = getIntent().getStringExtra("id");

        userDatabase = FirebaseDatabase.getInstance().getReference("user");
        foodDatabase = FirebaseDatabase.getInstance().getReference("food");

        //Manual enter food
        btnAddManual = findViewById(R.id.btnAddManual);
        editTextFoodNameByUser = findViewById(R.id.editTextFoodNameByUser);
        editTextCaloriesByUser = findViewById(R.id.editTextCaloriesByUser);

//        btnAddManual.setOnClickListener(this);
//        btnAddSearch.setOnClickListener(this);
//        btnCalculator.setOnClickListener(this);
//        btnLogout.setOnClickListener(this);
//
        textViewDinner.setOnClickListener(this);
        textViewLunch.setOnClickListener(this);
        textViewBreakfast.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnLogout:
                logout();
                break;
            case R.id.btnCalculator:
                calculator();
                break;
            case R.id.rbBreakfast:
                selection = rbBreakfast.getText().toString();
                break;
            case R.id.rbLunch:
                selection = rbLunch.getText().toString();
                break;
            case R.id.rbDinner:
                selection = rbDinner.getText().toString();
                break;
            case R.id.btnAddSearch:
                searchFood();
                break;
            case R.id.btnAddManual:
                createFood();
                break;
            case R.id.textViewBreakfast:
                ShowDetailList("breakfast");
                break;
            case R.id.textViewLunch:
                ShowDetailList("lunch");
                break;
            case R.id.textViewDinner:
                ShowDetailList("dinner");
                break;

        }
    }

    private void ShowDetailList(String time){

        Intent intent = new Intent(HomeContext, Recipe.class);

        if(time == "breakfast"){
            intent.putExtra("foodname", TotalBreakfast);
        }else if(time == "lunch"){
            intent.putExtra("foodname", TotalLunch);
        }else{
            intent.putExtra("foodname", TotalDinner);
        }

        startActivity(intent);
    }


    private void logout() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void calculator() {
        userChild = FirebaseDatabase.getInstance().getReference("user").child(String.valueOf(userId));

        ValueEventListener FoodListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    weight = Integer.valueOf(dataSnapshot.child("weight").getValue().toString());

                    Intent intent = new Intent(HomeContext, Calculator.class);
                    intent.putExtra("id", userId);
                    intent.putExtra("weight", String.valueOf(weight));
                    //!! here is a get String Extra, always put a String type in intent, other wise it will not send the value(which is null)

                    startActivity(intent);
                } else {
                    Toast.makeText(HomeContext,
                            "Fail to find user data from database",
                            LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };

        userChild.addValueEventListener(FoodListener);
    }

    private void searchFood() {

        String inputFoodName = editTextSearch.getText().toString();

        SpecificfoodChild = FirebaseDatabase.getInstance().getReference("food").child(inputFoodName);

        ValueEventListener FoodListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String foodname = dataSnapshot.child("name").getValue().toString();
                    String kcal = dataSnapshot.child("kcal").getValue().toString();

                    //create a reference to an auto-generated child location, and get the key value
                    String key = userDatabase.child("logfood").push().getKey();

                    Food tmpFood = new Food();
                    tmpFood.setKcal(Float.valueOf(kcal));
                    tmpFood.setName(foodname);




                    userDatabase.child(userId).child("foodlog")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (selection.equals("Breakfast")) {
                                        userDatabase.child(userId).child("foodlog").child("breakfast").child(key).setValue(tmpFood);
                                        textViewBreakfast.append(foodname + " " + kcal + " Kcal" + "\n");
                                        TotalBreakfast = TotalBreakfast + foodname + ",";
                                    } else if (selection.equals("Lunch")) {
                                        userDatabase.child(userId).child("foodlog").child("lunch").child(key).setValue(tmpFood);
                                        textViewLunch.append(foodname + " " + kcal + " Kcal" + "\n");
                                        TotalLunch = TotalLunch + foodname + ",";
                                    } else if (selection.equals("Dinner")) {
                                        userDatabase.child(userId).child("foodlog").child("dinner").child(key).setValue(tmpFood);
                                        textViewDinner.append(foodname + " " + kcal + " Kcal" + "\n");
                                        TotalDinner = TotalDinner + foodname + ",";
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        SpecificfoodChild.orderByChild("name").addListenerForSingleValueEvent(FoodListener);
    }

    //THE KCAL WITH FLOAT VALUE CANNOT BE ADD INTO FOOD LOG


    private void createFood(){
        /**
         * This method create a Food object which contains food information entered by user.
         * After create Food object, save data to firebase
         * @param non.
         * @return non.
         */

        try{

            //create food object
            Food food = new Food();

            //set value to object from ui EditText
            food.setName(editTextFoodNameByUser.getText().toString());
            food.setKcal(Float.valueOf(editTextCaloriesByUser.getText().toString()));

            //add the object food to the collection food
            foodDatabase.child(food.getName()).setValue(food);

            //display the message to user
            Toast.makeText(this,
                    food.getName() + " is added successfully to the database",
                    Toast.LENGTH_LONG).show();


            editTextFoodNameByUser.setText(null);
            editTextCaloriesByUser.setText(null);
            editTextSearch.requestFocus();

            String key = userDatabase.child("logfood").push().getKey();

            if (selection.equals("Breakfast")) {
                userDatabase.child(userId).child("foodlog").child("breakfast").child(key).setValue(food);
                textViewBreakfast.append(food.getName() + " " + food.getKcal() + " Kcal" + "\n");
                TotalBreakfast = TotalBreakfast + food.getName() + ",";
            } else if (selection.equals("Lunch")) {
                userDatabase.child(userId).child("foodlog").child("lunch").child(key).setValue(food);
                textViewLunch.append(food.getName() + " " + food.getKcal() + " Kcal" + "\n");
                TotalLunch = TotalLunch + food.getName() + ",";
            } else if (selection.equals("Dinner")) {
                userDatabase.child(userId).child("foodlog").child("dinner").child(key).setValue(food);
                textViewDinner.append(food.getName() + " " + food.getKcal() + " Kcal" + "\n");
                TotalDinner = TotalDinner +  food.getName() + ",";
            }

        }catch (Exception e){
            Toast.makeText(this,
                    "Ops, something went wrong",
                    Toast.LENGTH_LONG).show();

            Log.d("FIREBASE","Unable to complete action, error message: " + e.getMessage());

        }
    }
}