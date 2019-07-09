package com.example.calog.VO;

import java.sql.Date;

public class UserTotalCaloriesViewVO {

    private String userId;
    private String name;
    private double sumDietCalorie;
    private Date dietDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSumDietCalorie() {
        return sumDietCalorie;
    }

    public void setSumDietCalorie(int sumDietCalorie) {
        this.sumDietCalorie = sumDietCalorie;
    }

    public Date getDietDate() {
        return dietDate;
    }

    public void setDietDate(Date dietDate) {
        this.dietDate = dietDate;
    }

    @Override
    public String toString() {
        return "UserTotalCaloriesViewVO{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", sumDietCalorie=" + sumDietCalorie +
                ", dietDate=" + dietDate +
                '}';
    }
}
