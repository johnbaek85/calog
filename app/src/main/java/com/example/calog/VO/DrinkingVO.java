package com.example.calog.VO;

import java.sql.Date;

public class DrinkingVO {

    private String user_id;
    private Date drinking_date;
    private double alcohol_content;

	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public Date getDrinking_date() {
		return drinking_date;
	}
	public void setDrinking_date(Date drinking_date) {
		this.drinking_date = drinking_date;
	}
	public double getAlcohol_content() {
		return alcohol_content;
	}
	public void setAlcohol_content(double alcohol_content) {
		this.alcohol_content = alcohol_content;
	}

	@Override
	public String toString() {
		return "DrinkingVO{" +
				"user_id='" + user_id + '\'' +
				", drinking_date=" + drinking_date +
				", alcohol_content=" + alcohol_content +
				'}';
	}
}
