package com.example.calog.VO;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFitnessMenuName() {
        return fitnessMenuName;
    }

    public void setFitnessMenuName(String fitnessMenuName) {
        this.fitnessMenuName = fitnessMenuName;
    }

    public String getFitnessTypeName() {
        return fitnessTypeName;
    }

    public void setFitnessTypeName(String fitnessTypeName) {
        this.fitnessTypeName = fitnessTypeName;
    }

    public Date getFitnessDate() {
        return FitnessDate;
    }

    public void setFitnessDate(Date fitnessDate) {
        FitnessDate = fitnessDate;
    }

    public int getFitnessMenuId() {
        return FitnessMenuId;
    }

    public void setFitnessMenuId(int fitnessMenuId) {
        FitnessMenuId = fitnessMenuId;
    }

    public int getFitnessHours() {
        return FitnessHours;
    }

    public void setFitnessHours(int fitnessHours) {
        FitnessHours = fitnessHours;
    }

    public int getUsedCalorie() {
        return usedCalorie;
    }

    public void setUsedCalorie(int usedCalorie) {
        this.usedCalorie = usedCalorie;
    }

    public int getFitnessTypeId() {
        return FitnessTypeId;
    }

    public void setFitnessTypeId(int fitnessTypeId) {
        FitnessTypeId = fitnessTypeId;
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

    @Override
    public String toString() {
        return "FitnessVO{" +
                "userId='" + userId + '\'' +
                ", fitnessMenuName='" + fitnessMenuName + '\'' +
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
