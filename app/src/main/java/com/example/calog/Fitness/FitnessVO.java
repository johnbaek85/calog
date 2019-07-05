package com.example.calog.Fitness;

import java.sql.Date;

public class FitnessVO {
    private String userId;
    private String fitnessMenuName;
    private String fitnessTypeName;
    private Date FitnessDate;
    private int FitnessMenuId;
    private int FitnessHours;
    private int usedCalorie;
    private int FitnessTypeId;
    private int distance;
    private int numberSteps;


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFitnessMenuName(String fitnessMenuNmae) {
        this.fitnessMenuName = fitnessMenuNmae;
    }

    public void setFitnessTypeName(String fitnessTypeName) {
        this.fitnessTypeName = fitnessTypeName;
    }

    public void setFitnessDate(Date fitnessDate) {
        FitnessDate = fitnessDate;
    }

    public void setFitnessMenuId(int fitnessMenuId) {
        FitnessMenuId = fitnessMenuId;
    }

    public void setFitnessHours(int fitnessHours) {
        FitnessHours = fitnessHours;
    }

    public void setUsedCalorie(int usedCalorie) {
        this.usedCalorie = usedCalorie;
    }

    public void setFitnessTypeId(int fitnessTypeId) {
        FitnessTypeId = fitnessTypeId;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setNumberSteps(int numberSteps) {
        this.numberSteps = numberSteps;
    }

    public String getUserId() {
        return userId;
    }

    public String getFitnessMenuName() {
        return fitnessMenuName;
    }

    public String getFitnessTypeName() {
        return fitnessTypeName;
    }

    public Date getFitnessDate() {
        return FitnessDate;
    }

    public int getFitnessMenuId() {
        return FitnessMenuId;
    }

    public int getFitnessHours() {
        return FitnessHours;
    }

    public int getUsedCalorie() {
        return usedCalorie;
    }

    public int getFitnessTypeId() {
        return FitnessTypeId;
    }

    public int getDistance() {
        return distance;
    }

    public int getNumberSteps() {
        return numberSteps;
    }

    @Override
    public String toString() {
        return "FitnessVO{" +
                "userId='" + userId + '\'' +
                ", fitnessMenuNmae='" + fitnessMenuName + '\'' +
                ", fitnessTypeName='" + fitnessTypeName + '\'' +
                ", FitnessDate=" + FitnessDate +
                ", FitnessMenuId=" + FitnessMenuId +
                ", FitnessHours=" + FitnessHours +
                ", usedCalorie=" + usedCalorie +
                ", FitnessTypeId=" + FitnessTypeId +
                ", distance=" + distance +
                ", numberSteps=" + numberSteps +
                '}';
    }
}
