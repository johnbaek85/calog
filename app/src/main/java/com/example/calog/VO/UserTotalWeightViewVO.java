package com.example.calog.VO;

import java.sql.Date;

public class UserTotalWeightViewVO {

    private String user_id;
    private String name;
    private String fitness_type_name;
    private int sum_weight_seconds;
    private double sum_weight_used_calorie;
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
	public String getFitness_type_name() {
		return fitness_type_name;
	}
	public void setFitness_type_name(String fitness_type_name) {
		this.fitness_type_name = fitness_type_name;
	}
	public int getSum_weight_seconds() {
		return sum_weight_seconds;
	}
	public void setSum_weight_seconds(int sum_weight_seconds) {
		this.sum_weight_seconds = sum_weight_seconds;
	}
	public double getSum_weight_used_calorie() {
		return sum_weight_used_calorie;
	}
	public void setSum_weight_used_calorie(double sum_weight_used_calorie) {
		this.sum_weight_used_calorie = sum_weight_used_calorie;
	}
	public Date getFitness_date() {
		return fitness_date;
	}
	public void setFitness_date(Date fitness_date) {
		this.fitness_date = fitness_date;
	}

	@Override
	public String toString() {
		return "UserTotalWeightViewVO{" +
				"user_id='" + user_id + '\'' +
				", name='" + name + '\'' +
				", fitness_type_name='" + fitness_type_name + '\'' +
				", sum_weight_seconds=" + sum_weight_seconds +
				", sum_weight_used_calorie=" + sum_weight_used_calorie +
				", fitness_date=" + fitness_date +
				'}';
	}
}