package com.example.calog.VO;

import java.sql.Date;

public class DietVO {

    private int diet_id;
    private String user_id;
    private Date diet_date;
    private int diet_type_id;
    private int diet_menu_id;
    private int diet_amount;
    private int sum_calorie;
    
	public int getDiet_id() {
		return diet_id;
	}
	public void setDiet_id(int diet_id) {
		this.diet_id = diet_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public Date getDiet_date() {
		return diet_date;
	}
	public void setDiet_date(Date diet_date) {
		this.diet_date = diet_date;
	}
	public int getDiet_type_id() {
		return diet_type_id;
	}
	public void setDiet_type_id(int diet_type_id) {
		this.diet_type_id = diet_type_id;
	}
	public int getDiet_menu_id() {
		return diet_menu_id;
	}
	public void setDiet_menu_id(int diet_menu_id) {
		this.diet_menu_id = diet_menu_id;
	}
	public int getDiet_amount() {
		return diet_amount;
	}
	public void setDiet_amount(int diet_amount) {
		this.diet_amount = diet_amount;
	}
	public int getSum_calorie() {
		return sum_calorie;
	}
	public void setSum_calorie(int sum_calorie) {
		this.sum_calorie = sum_calorie;
	}

   
    
}
