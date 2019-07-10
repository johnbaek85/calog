package com.example.calog.VO;

import java.sql.Date;

public class UserTotalCardioViewVO {

    private String user_id;
    private String name;
    private int sum_cardio_seconds;
    private int sum_cardio_distance;
    private int sum_cardio_number_steps;
    private double sum_cardio_used_calorie;
    private Date fitness_date;
    
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
	public int getSum_cardio_seconds() {
		return sum_cardio_seconds;
	}
	public void setSum_cardio_seconds(int sum_cardio_seconds) {
		this.sum_cardio_seconds = sum_cardio_seconds;
	}
	public int getSum_cardio_distance() {
		return sum_cardio_distance;
	}
	public void setSum_cardio_distance(int sum_cardio_distance) {
		this.sum_cardio_distance = sum_cardio_distance;
	}
	public int getSum_cardio_number_steps() {
		return sum_cardio_number_steps;
	}
	public void setSum_cardio_number_steps(int sum_cardio_number_steps) {
		this.sum_cardio_number_steps = sum_cardio_number_steps;
	}
	public double getSum_cardio_used_calorie() {
		return sum_cardio_used_calorie;
	}
	public void setSum_cardio_used_calorie(double sum_cardio_used_calorie) {
		this.sum_cardio_used_calorie = sum_cardio_used_calorie;
	}
	public Date getFitness_date() {
		return fitness_date;
	}
	public void setFitness_date(Date fitness_date) {
		this.fitness_date = fitness_date;
	}
}
