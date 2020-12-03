package com.example.nutritiouslife;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import model.Food;
import model.RecipeAdapter;

import static android.widget.Toast.LENGTH_LONG;

public class Recipe extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lvRecipes;
    ArrayList<Food> listOfFood;
    RecipeAdapter recipeAdapter;

    String foodlist;
    String[] foodlistsplit;

    DatabaseReference RefFoodDatabase,RefFoodChild;

    Context HomeContext = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        intialize();
    }
    private void intialize() {
        RefFoodDatabase = FirebaseDatabase.getInstance().getReference("food");

        foodlist = getIntent().getStringExtra("foodname");

        listOfFood = SeperateFoodInArrayList(foodlist);

        lvRecipes = findViewById(R.id.lvRecipes);
        lvRecipes.setOnItemClickListener(this);

        recipeAdapter = new RecipeAdapter(this,listOfFood);

        lvRecipes.setAdapter(recipeAdapter);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
        Toast.makeText(this,listOfFood.get(i).getName(),Toast.LENGTH_LONG).show();

    }

    private ArrayList<Food> SeperateFoodInArrayList(String foodlist){

        ArrayList<Food> FoodArrayList = new ArrayList<Food>();

        foodlistsplit = foodlist.split(",");

        ValueEventListener FoodListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Food food = new Food();

                for (int i = 0; i < foodlistsplit.length; i++) {

                    String name = foodlistsplit[i];

                    RefFoodChild = RefFoodDatabase.child(name);

                    Toast.makeText(HomeContext, name, Toast.LENGTH_LONG).show();

                    if(dataSnapshot.exists()) {
                        food.setName(dataSnapshot.child(name).child("name").getValue().toString());
                        food.setKcal(Float.valueOf(dataSnapshot.child(name).child("kcal").getValue().toString()));
                        food.setPhoto(dataSnapshot.child(name).child("photo").getValue().toString());
                    }
                    FoodArrayList.add(food);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };

        RefFoodDatabase.addValueEventListener(FoodListener);

        return FoodArrayList;
    }
}

