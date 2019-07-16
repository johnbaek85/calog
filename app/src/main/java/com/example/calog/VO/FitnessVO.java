package com.example.calog.VO;

import java.sql.Date;

public class FitnessVO {

	private String user_id;
	private String name;
	private int fitness_cardio_id;
	private int fitness_weight_id;
	private String fitness_date;
	private int fitness_menu_id;
	private String fitness_menu_name;
	private String fitness_menu_image;
	private double unit_calorie;
	private int fitness_type_id;
	private String fitness_type_name;
	private int fitness_seconds;
	private int distance;
	private int number_steps;
	private double used_calorie;
	private int sum_cardio_seconds;
	private int sum_cardio_distance;
	private int sum_cardio_number_steps;
	private double sum_cardio_used_calorie;
	private int sum_weight_seconds;
	private double sum_weight_used_calorie;
	private double total_cardio_weight_calorie;

	private String cardio_fitness_date;
	private String weight_fitness_date;

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

	public int getFitness_cardio_id() {
		return fitness_cardio_id;
	}

	public void setFitness_cardio_id(int fitness_cardio_id) {
		this.fitness_cardio_id = fitness_cardio_id;
	}

	public int getFitness_weight_id() {
		return fitness_weight_id;
	}

	public void setFitness_weight_id(int fitness_weight_id) {
		this.fitness_weight_id = fitness_weight_id;
	}

	public String getFitness_date() {
		return fitness_date;
	}

	public void setFitness_date(String fitness_date) {
		this.fitness_date = fitness_date;
	}

	public int getFitness_menu_id() {
		return fitness_menu_id;
	}

	public void setFitness_menu_id(int fitness_menu_id) {
		this.fitness_menu_id = fitness_menu_id;
	}

	public String getFitness_menu_name() {
		return fitness_menu_name;
	}

	public void setFitness_menu_name(String fitness_menu_name) {
		this.fitness_menu_name = fitness_menu_name;
	}

	public String getFitness_menu_image() {
		return fitness_menu_image;
	}

	public void setFitness_menu_image(String fitness_menu_image) {
		this.fitness_menu_image = fitness_menu_image;
	}

	public double getUnit_calorie() {
		return unit_calorie;
	}

	public void setUnit_calorie(double unit_calorie) {
		this.unit_calorie = unit_calorie;
	}

	public int getFitness_type_id() {
		return fitness_type_id;
	}

	public void setFitness_type_id(int fitness_type_id) {
		this.fitness_type_id = fitness_type_id;
	}

	public String getFitness_type_name() {
		return fitness_type_name;
	}

	public void setFitness_type_name(String fitness_type_name) {
		this.fitness_type_name = fitness_type_name;
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


	public double getTotal_cardio_weight_calorie() {
		return total_cardio_weight_calorie;
	}

	public void setTotal_cardio_weight_calorie(double total_cardio_weight_calorie) {
		this.total_cardio_weight_calorie = total_cardio_weight_calorie;
	}

	public String getCardio_fitness_date() {
		return cardio_fitness_date;
	}

	public void setCardio_fitness_date(String cardio_fitness_date) {
		this.cardio_fitness_date = cardio_fitness_date;
	}

	public String getWeight_fitness_date() {
		return weight_fitness_date;
	}

	public void setWeight_fitness_date(String weight_fitness_date) {
		this.weight_fitness_date = weight_fitness_date;
	}

	@Override
	public String toString() {
		return "FitnessVO{" +
				"user_id='" + user_id + '\'' +
				", name='" + name + '\'' +
				", fitness_cardio_id=" + fitness_cardio_id +
				", fitness_weight_id=" + fitness_weight_id +
				", fitness_date='" + fitness_date + '\'' +
				", fitness_menu_id=" + fitness_menu_id +
				", fitness_menu_name='" + fitness_menu_name + '\'' +
				", fitness_menu_image='" + fitness_menu_image + '\'' +
				", unit_calorie=" + unit_calorie +
				", fitness_type_id=" + fitness_type_id +
				", fitness_type_name='" + fitness_type_name + '\'' +
				", fitness_seconds=" + fitness_seconds +
				", distance=" + distance +
				", number_steps=" + number_steps +
				", used_calorie=" + used_calorie +
				", sum_cardio_seconds=" + sum_cardio_seconds +
				", sum_cardio_distance=" + sum_cardio_distance +
				", sum_cardio_number_steps=" + sum_cardio_number_steps +
				", sum_cardio_used_calorie=" + sum_cardio_used_calorie +
				", sum_weight_seconds=" + sum_weight_seconds +
				", sum_weight_used_calorie=" + sum_weight_used_calorie +
				", total_cardio_weight_calorie=" + total_cardio_weight_calorie +
				", cardio_fitness_date='" + cardio_fitness_date + '\'' +
				", weight_fitness_date='" + weight_fitness_date + '\'' +
				'}';
	}
}
