package com.example.calog.VO;

import java.sql.Date;

public class UserTotalCardioViewVO {

    private String userId;
    private String name;
    private int sumCardioSeconds;
    private int sumCardioDistance;
    private int sumCardioNumberSteps;
    private double sumCardioUsedCalorie;
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

    public int getSumCardioSeconds() {
        return sumCardioSeconds;
    }

    public void setSumCardioSeconds(int sumCardioSeconds) {
        this.sumCardioSeconds = sumCardioSeconds;
    }

    public int getSumCardioDistance() {
        return sumCardioDistance;
    }

    public void setSumCardioDistance(int sumCardioDistance) {
        this.sumCardioDistance = sumCardioDistance;
    }

    public int getSumCardioNumberSteps() {
        return sumCardioNumberSteps;
    }

    public void setSumCardioNumberSteps(int sumCardioNumberSteps) {
        this.sumCardioNumberSteps = sumCardioNumberSteps;
    }

    public double getSumCardioUsedCalorie() {
        return sumCardioUsedCalorie;
    }

    public void setSumCardioUsedCalorie(double sumCardioUsedCalorie) {
        this.sumCardioUsedCalorie = sumCardioUsedCalorie;
    }

    public Date getFitnessDate() {
        return fitnessDate;
    }

    public void setFitnessDate(Date fitnessDate) {
        this.fitnessDate = fitnessDate;
    }

    @Override
    public String toString() {
        return "UserTotalCardioViewVO{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", sumCardioSeconds=" + sumCardioSeconds +
                ", sumCardioDistance=" + sumCardioDistance +
                ", sumCardioNumberSteps=" + sumCardioNumberSteps +
                ", sumCardioUsedCalorie=" + sumCardioUsedCalorie +
                ", fitnessDate=" + fitnessDate +
                '}';
    }
}
