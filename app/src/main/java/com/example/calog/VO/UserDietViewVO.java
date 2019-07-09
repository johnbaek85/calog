package com.example.calog.VO;

import java.sql.Date;

public class UserDietViewVO {

    private String userId;
    private String name;
    private int dietTypeId;
    private String dietTypeName;
    private String dietMenuName;
    private int dietAmount;
    private int calorie;
    private int sumCalorie;
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

    public int getDietTypeId() {
        return dietTypeId;
    }

    public void setDietTypeId(int dietTypeId) {
        this.dietTypeId = dietTypeId;
    }

    public String getDietTypeName() {
        return dietTypeName;
    }

    public void setDietTypeName(String dietTypeName) {
        this.dietTypeName = dietTypeName;
    }

    public String getDietMenuName() {
        return dietMenuName;
    }

    public void setDietMenuName(String dietMenuName) {
        this.dietMenuName = dietMenuName;
    }

    public int getDietAmount() {
        return dietAmount;
    }

    public void setDietAmount(int dietAmount) {
        this.dietAmount = dietAmount;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public int getSumCalorie() {
        return sumCalorie;
    }

    public void setSumCalorie(int sumCalorie) {
        this.sumCalorie = sumCalorie;
    }

    public Date getDietDate() {
        return dietDate;
    }

    public void setDietDate(Date dietDate) {
        this.dietDate = dietDate;
    }

    @Override
    public String toString() {
        return "UserDietViewVO{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", dietTypeId=" + dietTypeId +
                ", dietTypeName='" + dietTypeName + '\'' +
                ", dietMenuName='" + dietMenuName + '\'' +
                ", dietAmount=" + dietAmount +
                ", calorie=" + calorie +
                ", sumCalorie=" + sumCalorie +
                ", dietDate=" + dietDate +
                '}';
    }
}
