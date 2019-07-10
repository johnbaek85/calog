package com.example.calog.VO;

import java.sql.Date;

public class FitnessVO {
    private String user_id;
    private String fitness_menu_name;
    private String fitness_type_name;
    private Date Fitness_date;
    private int Fitness_menu_id;
    private int Fitness_hours;
    private int used_calorie;
    private int fitness_type_id;
    private int distance;
    private int number_steps;
    
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getFitness_menu_name() {
		return fitness_menu_name;
	}
	public void setFitness_menu_name(String fitness_menu_name) {
		this.fitness_menu_name = fitness_menu_name;
	}
	public String getFitness_type_name() {
		return fitness_type_name;
	}
	public void setFitness_type_name(String fitness_type_name) {
		this.fitness_type_name = fitness_type_name;
	}
	public Date getFitness_date() {
		return Fitness_date;
	}
	public void setFitness_date(Date fitness_date) {
		Fitness_date = fitness_date;
	}
	public int getFitness_menu_id() {
		return Fitness_menu_id;
	}
	public void setFitness_menu_id(int fitness_menu_id) {
		Fitness_menu_id = fitness_menu_id;
	}
	public int getFitness_hours() {
		return Fitness_hours;
	}
	public void setFitness_hours(int fitness_hours) {
		Fitness_hours = fitness_hours;
	}
	public int getUsed_calorie() {
		return used_calorie;
	}
	public void setUsed_calorie(int used_calorie) {
		this.used_calorie = used_calorie;
	}
	public int getFitness_type_id() {
		return fitness_type_id;
	}
	public void setFitness_type_id(int fitness_type_id) {
		this.fitness_type_id = fitness_type_id;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getNumber_steps() {
		return number_steps;
	}
	public void setNumber_steps(int number_steps) {
		this.number_steps = number_steps;
	}

  
}
