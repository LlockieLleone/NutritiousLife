package model;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class Food {
    private String name;
    private Float kcal;
    private String photo;

    public Food(String name, Float kcal, String photo) {
        this.name = name;
        this.kcal = kcal;
        this.photo = photo;
    }

    public Food(String name, Float kcal){
        this.name = name;
        this.kcal = kcal;
    }

    public Food() {
        this.name = name;
        this.kcal = kcal;
        this.photo = photo;
    }

    @Override
    public String toString() {
        return getName()+getKcal();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(Float kcal) {
        this.kcal = kcal;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}

