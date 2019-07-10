package com.example.calog.VO;

import java.sql.Date;

public class UserDietViewVO {

    private String user_id;
    private String name;
    private int diet_type_id;
    private String diet_type_name;
    private String diet_menu_name;
    private int diet_amount;
    private int calorie;
    private int sum_calorie;
    private Date diet_date;
    
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDiet_type_id() {
		return diet_type_id;
	}
	public void setDiet_type_id(int diet_type_id) {
		this.diet_type_id = diet_type_id;
	}
	public String getDiet_type_name() {
		return diet_type_name;
	}
	public void setDiet_type_name(String diet_type_name) {
		this.diet_type_name = diet_type_name;
	}
	public String getDiet_menu_name() {
		return diet_menu_name;
	}
	public void setDiet_menu_name(String diet_menu_name) {
		this.diet_menu_name = diet_menu_name;
	}
	public int getDiet_amount() {
		return diet_amount;
	}
	public void setDiet_amount(int diet_amount) {
		this.diet_amount = diet_amount;
	}
	public int getCalorie() {
		return calorie;
	}
	public void setCalorie(int calorie) {
		this.calorie = calorie;
	}
	public int getSum_calorie() {
		return sum_calorie;
	}
	public void setSum_calorie(int sum_calorie) {
		this.sum_calorie = sum_calorie;
	}
	public Date getDiet_date() {
		return diet_date;
	}
	public void setDiet_date(Date diet_date) {
		this.diet_date = diet_date;
	}

	@Override
	public String toString() {
		return "UserDietViewVO{" +
				"user_id='" + user_id + '\'' +
				", name='" + name + '\'' +
				", diet_type_id=" + diet_type_id +
				", diet_type_name='" + diet_type_name + '\'' +
				", diet_menu_name='" + diet_menu_name + '\'' +
				", diet_amount=" + diet_amount +
				", calorie=" + calorie +
				", sum_calorie=" + sum_calorie +
				", diet_date=" + diet_date +
				'}';
	}
}
