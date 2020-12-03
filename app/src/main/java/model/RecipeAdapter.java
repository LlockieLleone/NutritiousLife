package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nutritiouslife.R;

import java.util.ArrayList;

public class RecipeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Recipe> listOfRecipes;

    Recipe recipe;

    public RecipeAdapter(Context context, ArrayList<Recipe> listOfRecipes) {
        this.context = context;
        this.listOfRecipes = listOfRecipes;
    }

    @Override
    public int getCount() {
        return listOfRecipes.size();
    }

    @Override
    public Object getItem(int i) {
        return listOfRecipes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View oneItem;

        ImageView photo;
        TextView foodName, calories;

        LayoutInflater inflater = LayoutInflater.from(context);

        oneItem = inflater.inflate(R.layout.one_item,viewGroup,false);

        photo = oneItem.findViewById(R.id.ivPhoto);
        foodName = oneItem.findViewById(R.id.tvRecipes);
        calories = oneItem.findViewById(R.id.tvCalories);

        recipe = (Recipe)getItem(i);

        foodName.setText(recipe.getFoodName());
        calories.setText(recipe.getCalories());

        String photoName = (recipe.getPhoto());

        int photoResourceId = context
                .getResources()
                getIdentifier("drawable/"+photoName,null,context.getPackageName());

        photo.setImageResource(photoResourceId);

        return oneItem;
    }
}
