package com.example.calog.Diet;

public class DietMenuVO {

    private int dietMenuId;
    private String dietMenuName;
    private int calorie;

    public DietMenuVO() {}

    public DietMenuVO(String dietMenuName, int calorie) {
        this.dietMenuName = dietMenuName;
        this.calorie = calorie;
    }

    public int getDietMenuId() {
        return dietMenuId;
    }

    public void setDietMenuId(int dietMenuId) {
        this.dietMenuId = dietMenuId;
    }

    public String getDietMenuName() {
        return dietMenuName;
    }

    public void setDietMenuName(String dietMenuName) {
        this.dietMenuName = dietMenuName;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }
}
