package model;

import java.util.HashMap;
import java.util.Map;

public class User {
    private int id;
    private String email;
    private String firstname;
    private String lastname;
    private int age;
    private int weight;
    private int height;
    private String password;
    private String foodname;
    private double kcal;


    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(int id, String email, String firstname, String lastname, int age, int weight, int height, String password) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.weight = weight;
        this.height = height;
    }


    public User(String foodname, float kcal) {
        this.foodname = foodname;
        this.kcal = kcal;
    }


    public Map<String, Object> toMapLogFood() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", foodname);
        result.put("kcal", kcal);
        return result;
    }

    public User(String id, String email, String firstname, String lastname, int age, int weight, int height, String password) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }
}

