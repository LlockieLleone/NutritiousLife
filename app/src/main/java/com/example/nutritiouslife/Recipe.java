package com.example.nutritiouslife;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import model.FileManagement;
import model.RecipeAdapter;

public class Recipe extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lvRecipes;
    ArrayList<model.Recipe> listOfRecipes;
    RecipeAdapter recipeAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        intialize();
    }

    private void intialize() {
        lvRecipes = findViewById(R.id.lvRecipes);
        lvRecipes.setOnItemClickListener(this);
        listOfRecipes = FileManagement.readFile(this,"recipes.txt");
        recipeAdapter = new RecipeAdapter(this,listOfRecipes);
        lvRecipes.setAdapter(recipeAdapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

        Toast.makeText(this,listOfRecipes.get(i).getFoodName(),Toast.LENGTH_LONG).show();


    }
}