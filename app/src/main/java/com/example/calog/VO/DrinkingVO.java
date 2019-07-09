package com.example.calog.VO;

import java.sql.Date;

public class DrinkingVO {

    private String userId;
    private Date drinkingDate;
    private double alcoholContent;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDrinkingDate() {
        return drinkingDate;
    }

    public void setDrinkingDate(Date drinkingDate) {
        this.drinkingDate = drinkingDate;
    }

    public double getAlcoholContent() {
        return alcoholContent;
    }

    public void setAlcoholContent(double alcoholContent) {
        this.alcoholContent = alcoholContent;
    }

    @Override
    public String toString() {
        return "DrinkingVO{" +
                "userId='" + userId + '\'' +
                ", drinkingDate=" + drinkingDate +
                ", alcoholContent=" + alcoholContent +
                '}';
    }
}
