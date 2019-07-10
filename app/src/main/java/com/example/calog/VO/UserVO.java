package com.example.calog.VO;

import java.sql.Date;

public class UserVO {

    private String user_id;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Date birthday;
    private String gender;
    private double height;
    private double weight;
    private double bmi;
    private int del_check_user_id;
    
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getBmi() {
		return bmi;
	}
	public void setBmi(double bmi) {
		this.bmi = bmi;
	}
	public int getDel_check_user_id() {
		return del_check_user_id;
	}
	public void setDel_check_user_id(int del_check_user_id) {
		this.del_check_user_id = del_check_user_id;
	}

	@Override
	public String toString() {
		return "UserVO{" +
				"user_id='" + user_id + '\'' +
				", password='" + password + '\'' +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", phone='" + phone + '\'' +
				", address='" + address + '\'' +
				", birthday=" + birthday +
				", gender='" + gender + '\'' +
				", height=" + height +
				", weight=" + weight +
				", bmi=" + bmi +
				", del_check_user_id=" + del_check_user_id +
				'}';
	}
}
