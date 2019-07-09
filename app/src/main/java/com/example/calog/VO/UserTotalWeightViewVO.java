package com.example.calog.VO;

import java.sql.Date;

public class UserTotalWeightViewVO {

    private String userId;
    private String name;
    private String fitnessTypeName;
    private int sumWeightSeconds;
    private double sumWeightUsedCalorie;
    private Date fitnessDate;

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

    public String getFitnessTypeName() {
        return fitnessTypeName;
    }

    public void setFitnessTypeName(String fitnessTypeName) {
        this.fitnessTypeName = fitnessTypeName;
    }

    public int getSumWeightSeconds() {
        return sumWeightSeconds;
    }

    public void setSumWeightSeconds(int sumWeightSeconds) {
        this.sumWeightSeconds = sumWeightSeconds;
    }

    public double getSumWeightUsedCalorie() {
        return sumWeightUsedCalorie;
    }

    public void setSumWeightUsedCalorie(double sumWeightUsedCalorie) {
        this.sumWeightUsedCalorie = sumWeightUsedCalorie;
    }

    public Date getFitnessDate() {
        return fitnessDate;
    }

    public void setFitnessDate(Date fitnessDate) {
        this.fitnessDate = fitnessDate;
    }

    @Override
    public String toString() {
        return "UserTotalWeightViewVO{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", fitnessTypeName='" + fitnessTypeName + '\'' +
                ", sumWeightSeconds=" + sumWeightSeconds +
                ", sumWeightUsedCalorie=" + sumWeightUsedCalorie +
                ", fitnessDate=" + fitnessDate +
                '}';
    }
}
