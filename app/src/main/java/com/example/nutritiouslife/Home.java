package com.example.nutritiouslife;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class Home extends AppCompatActivity implements View.OnClickListener, ValueEventListener {
    Button btnLogout, btnCalculator, btnAddSearch, btnAddManual;
    String userId = "";
    private int weight;
    EditText editTextSearch, editTextFoodNameByUser, editTextCaloriesByUser;
    TextView textViewBreakfast;
    RadioButton rbBreakfast, rbLunch, rbDinner;
    String selection = "";


    DatabaseReference userDatabase, userChild, foodDatabase, foodChild;

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

        userId = getIntent().getStringExtra("id");

        userDatabase = FirebaseDatabase.getInstance().getReference("user");
        foodDatabase = FirebaseDatabase.getInstance().getReference("food");

        //Manual enter food
        btnAddManual = findViewById(R.id.btnAddManual);
        editTextFoodNameByUser = findViewById(R.id.editTextFoodNameByUser);
        editTextCaloriesByUser = findViewById(R.id.editTextCaloriesByUser);
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
        if (dataSnapshot.exists()) {

            weight = Integer.valueOf(dataSnapshot.child("weight").getValue().toString());

            Intent intent = new Intent(this, Calculator.class);
            intent.putExtra("id", userId);
            intent.putExtra("weight", String.valueOf(weight));
            //!! here is a get String Extra, always put a String type in intent, other wise it will not send the value(which is null)

            startActivity(intent);
        } else {
            Toast.makeText(this,
                    "Fail to find user data from database",
                    LENGTH_LONG).show();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
    //THE KCAL WITH FLOAT VALUE CANNOT BE ADD INTO FOOD LOG
    private void searchFood() {
        String inputFoodName = editTextSearch.getText().toString();
        foodChild = FirebaseDatabase.getInstance().getReference("food").child(inputFoodName);
        foodChild.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String foodname = dataSnapshot.child("name").getValue().toString();
                    String kcal = dataSnapshot.child("kcal").getValue().toString();

                    String key = userDatabase.child("logfood").push().getKey();
                    User user = new User(foodname, kcal);
                    Map<String, Object> logFoodValues = user.toMapLogFood();

                    Map<String, Object> logFoodUpdate = new HashMap<>();
                    logFoodUpdate.put("foodname " + foodname, logFoodValues);
                    logFoodUpdate.put("kcal " + kcal, logFoodValues);
                    userDatabase.child(userId).child("foodlog")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                {

                                    if(selection.equals("Breakfast")){
                                        userDatabase.child(userId).child("foodlog").child("breakfast").child(key).setValue(logFoodUpdate);
                                        textViewBreakfast.append(foodname+"\n"+kcal);                                    }
                                    else if(selection.equals("Lunch")){
                                        userDatabase.child(userId).child("foodlog").child("lunch").child(key).setValue(logFoodUpdate);
                                        textViewBreakfast.append(foodname+"\n"+kcal);
                                    }
                                    else if(selection.equals("Dinner")){
                                        userDatabase.child(userId).child("foodlog").child("dinner").child(key).setValue(logFoodUpdate);
                                        textViewBreakfast.append(foodname+"\n"+kcal);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){
            }
        });
    }

    private Food createFood(){
        /**
         * This method returns a Food object which contains food information entered by user.
         * @param non.
         * @return Food object.
         */

        Food food = new Food();
        food.setName(editTextFoodNameByUser.getText().toString());
        food.setKcal(Float.valueOf(editTextCaloriesByUser.getText().toString()));

        return food;
    }
}