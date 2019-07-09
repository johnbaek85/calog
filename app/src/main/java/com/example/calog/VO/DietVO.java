package com.example.calog.VO;

import java.sql.Date;

public class DietVO {

    private int dietId;
    private String userId;
    private Date dietDate;
    private int dietTypeId;
    private int dietMenuId;
    private int dietAmount;
    private int sumCalorie;

    public int getDietId() {
        return dietId;
    }

    public void setDietId(int dietId) {
        this.dietId = dietId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDietDate() {
        return dietDate;
    }

    public void setDietDate(Date dietDate) {
        this.dietDate = dietDate;
    }

    public int getDietTypeId() {
        return dietTypeId;
    }

    public void setDietTypeId(int dietTypeId) {
        this.dietTypeId = dietTypeId;
    }

    public int getDietMenuId() {
        return dietMenuId;
    }

    public void setDietMenuId(int dietMenuId) {
        this.dietMenuId = dietMenuId;
    }

    public int getDietAmount() {
        return dietAmount;
    }

    public void setDietAmount(int dietAmount) {
        this.dietAmount = dietAmount;
    }

    public int getSumCalorie() {
        return sumCalorie;
    }

    public void setSumCalorie(int sumCalorie) {
        this.sumCalorie = sumCalorie;
    }

    @Override
    public String toString() {
        return "DietVO{" +
                "dietId=" + dietId +
                ", userId='" + userId + '\'' +
                ", dietDate=" + dietDate +
                ", dietTypeId=" + dietTypeId +
                ", dietMenuId=" + dietMenuId +
                ", dietAmount=" + dietAmount +
                ", sumCalorie=" + sumCalorie +
                '}';
    }
}
