package com.example.calog.VO;

public class DietMenuVO {

    private int diet_menu_id;
    private String diet_menu_name;
    private int calorie;

    public DietMenuVO() { }

    public DietMenuVO(String diet_menu_name, int calorie) {
        this.diet_menu_name = diet_menu_name;
        this.calorie = calorie;
    }

    public int getDiet_menu_id() {
        return diet_menu_id;
    }

    public void setDiet_menu_id(int diet_menu_id) {
        this.diet_menu_id = diet_menu_id;
    }

    public String getDiet_menu_name() {
        return diet_menu_name;
    }

    public void setDiet_menu_name(String diet_menu_name) {
        this.diet_menu_name = diet_menu_name;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    @Override
    public String toString() {
        return "DietMenuVO{" +
                "diet_menu_id=" + diet_menu_id +
                ", diet_menu_name='" + diet_menu_name + '\'' +
                ", calorie=" + calorie +
                '}';
    }
}
