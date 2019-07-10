package com.example.calog.VO;

import java.sql.Date;

public class UserFitnessWeightViewVO {

    private String user_id;
    private String name;
    private String fitness_type_name;
    private String fitness_menu_name;
    private Date fitness_date;
    private int fitness_seconds;
    private double unit_calorie;
    
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
	public double getUnit_calorie() {
		return unit_calorie;
	}
	public void setUnit_calorie(double unit_calorie) {
		this.unit_calorie = unit_calorie;
	}

	@Override
	public String toString() {
		return "UserFitnessWeightViewVO{" +
				"user_id='" + user_id + '\'' +
				", name='" + name + '\'' +
				", fitness_type_name='" + fitness_type_name + '\'' +
				", fitness_menu_name='" + fitness_menu_name + '\'' +
				", fitness_date=" + fitness_date +
				", fitness_seconds=" + fitness_seconds +
				", unit_calorie=" + unit_calorie +
				'}';
	}
}
