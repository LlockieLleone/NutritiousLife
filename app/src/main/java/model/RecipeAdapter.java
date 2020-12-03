package model;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.nutritiouslife.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Food> listOfFoods;

    DatabaseReference RefFoodDatabase, RefFoodChild;

    String urlPhoto = "";

    Food food;

    public RecipeAdapter(Context context, ArrayList<Food> listOfFoods) {
        this.context = context;
        this.listOfFoods = listOfFoods;
    }

    @Override
    public int getCount() {
        return listOfFoods.size();
    }

    @Override
    public Object getItem(int i) {
        return listOfFoods.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View oneItem;

        ImageView photo;
        TextView foodName, calories;

        LayoutInflater inflater = LayoutInflater.from(context);

        oneItem = inflater.inflate(R.layout.one_item,viewGroup,false);

        photo = oneItem.findViewById(R.id.ivPhoto);
        foodName = oneItem.findViewById(R.id.tvRecipes);
        calories = oneItem.findViewById(R.id.tvCalories);

        food = (Food)getItem(position);

        foodName.setText(food.getName());
        calories.setText(String.valueOf(food.getKcal()));
        urlPhoto = food.getPhoto();

//        Picasso
//                .with(context)
//                .load(urlPhoto)
//                .placeholder(R.drawable.test)
//                .into(photo);

        RefFoodDatabase = FirebaseDatabase.getInstance().getReference("food");

        RefFoodChild = RefFoodDatabase.child(food.getName());

        return oneItem;
    }




}
