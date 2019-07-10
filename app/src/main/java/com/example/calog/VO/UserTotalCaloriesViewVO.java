package com.example.calog.VO;

import java.sql.Date;

public class UserTotalCaloriesViewVO {

    private String user_id;
    private String name;
    private double sum_calorie;
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
	public double getSum_calorie() {
		return sum_calorie;
	}
	public void setSum_calorie(double sum_calorie) {
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
		return "UserTotalCaloriesViewVO{" +
				"user_id='" + user_id + '\'' +
				", name='" + name + '\'' +
				", sum_calorie=" + sum_calorie +
				", diet_date=" + diet_date +
				'}';
	}
}
