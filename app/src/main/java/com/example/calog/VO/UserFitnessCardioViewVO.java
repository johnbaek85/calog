package com.example.calog.VO;

import java.sql.Date;

public class UserFitnessCardioViewVO {

    private String userId;
    private String name;
    private String fitnessTypeName;
    private String fitnessMenuName;
    private Date fitnessDate;
    private int fitnessSeconds;
    private int distance;
    private int numberSteps;
    private double usedCalorie;

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

    public String getFitnessMenuName() {
        return fitnessMenuName;
    }

    public void setFitnessMenuName(String fitnessMenuName) {
        this.fitnessMenuName = fitnessMenuName;
    }

    public Date getFitnessDate() {
        return fitnessDate;
    }

    public void setFitnessDate(Date fitnessDate) {
        this.fitnessDate = fitnessDate;
    }

    public int getFitnessSeconds() {
        return fitnessSeconds;
    }

    public void setFitnessSeconds(int fitnessSeconds) {
        this.fitnessSeconds = fitnessSeconds;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getNumberSteps() {
        return numberSteps;
    }

    public void setNumberSteps(int numberSteps) {
        this.numberSteps = numberSteps;
    }

    public double getUsedCalorie() {
        return usedCalorie;
    }

    public void setUsedCalorie(double usedCalorie) {
        this.usedCalorie = usedCalorie;
    }

    @Override
    public String toString() {
        return "UserFitnessCardioViewVO{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", fitnessTypeName='" + fitnessTypeName + '\'' +
                ", fitnessMenuName='" + fitnessMenuName + '\'' +
                ", fitnessDate=" + fitnessDate +
                ", fitnessSeconds=" + fitnessSeconds +
                ", distance=" + distance +
                ", numberSteps=" + numberSteps +
                ", usedCalorie=" + usedCalorie +
                '}';
    }
}
