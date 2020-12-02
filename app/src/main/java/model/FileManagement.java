package model;

import  android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import  java.util.ArrayList;
import java.util.StringTokenizer;

public class FileManagement {

    public static ArrayList<Recipe> readFile(Context context,String fileName){

        ArrayList<Recipe> list = new ArrayList<Recipe>();

        //Access to the Firebase Images folder

        AssetManager assM = FirebaseDatabase.getInstance();

        try{
            InputStreamReader isr = new InputStreamReader(assM.open(fileName));

            BufferedReader br = new BufferedReader(isr);

            String oneLine = br.readLine();
            while (oneLine != null){
                StringTokenizer st = new StringTokenizer(oneLine,",");
                String foodName = st.nextToken();
                String calories = st.nextToken();
                String photo = st.nextToken();

                Recipe recipe = new Recipe(foodName,calories,photo);
                list.add(recipe);

                oneLine = br.readLine();
            }

            br.close();
            isr.close();

        }catch (IOException e){
            Log.d("ERROR",e.getMessage());
        }

        return  list;

    }
}
