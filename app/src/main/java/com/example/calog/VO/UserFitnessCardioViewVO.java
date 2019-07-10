package com.example.calog.VO;

import java.sql.Date;

public class UserFitnessCardioViewVO {

    private String user_id;
    private String name;
    private String fitness_type_name;
    private String fitness_menu_name;
    private Date fitness_date;
    private int fitness_seconds;
    private int distance;
    private int number_steps;
    private double used_calorie;
    
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
	public String getFitness_type_name() {
		return fitness_type_name;
	}
	public void setFitness_type_name(String fitness_type_name) {
		this.fitness_type_name = fitness_type_name;
	}
	public String getFitness_menu_name() {
		return fitness_menu_name;
	}
	public void setFitness_menu_name(String fitness_menu_name) {
		this.fitness_menu_name = fitness_menu_name;
	}
	public Date getFitness_date() {
		return fitness_date;
	}
	public void setFitness_date(Date fitness_date) {
		this.fitness_date = fitness_date;
	}
	public int getFitness_seconds() {
		return fitness_seconds;
	}
	public void setFitness_seconds(int fitness_seconds) {
		this.fitness_seconds = fitness_seconds;
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
	public double getUsed_calorie() {
		return used_calorie;
	}
	public void setUsed_calorie(double used_calorie) {
		this.used_calorie = used_calorie;
	}

  
}
