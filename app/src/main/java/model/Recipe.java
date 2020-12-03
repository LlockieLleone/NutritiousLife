package model;

import androidx.annotation.NonNull;

public class Recipe {

    private  String foodName;
    private  String calories;
    private  String photo;

    public Recipe(String foodName, String calories, String photo) {
        this.foodName = foodName;
        this.calories = calories;
        this.photo = photo;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @NonNull
    @Override
    public String toString() {
        return foodName;
    }
}
