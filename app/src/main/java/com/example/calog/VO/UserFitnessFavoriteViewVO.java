package com.example.calog.VO;

public class UserFitnessFavoriteViewVO {

    private String userId;
    private String name;
    private String fitnessTypeName;
    private String fitnessMenuName;

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

    @Override
    public String toString() {
        return "UserFitnessFavoriteViewVO{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", fitnessTypeName='" + fitnessTypeName + '\'' +
                ", fitnessMenuName='" + fitnessMenuName + '\'' +
                '}';
    }
}
