package com.example.calog.VO;

import java.sql.Date;

public class MainHealthVO
{
    //식단 총 칼로리
    private double sum_calorie;

    //유산소 총 소모 칼로리
    private double sum_cardio_used_calorie;

    //무산소 총 소모 칼로리
    private double sum_weight_used_calorie;

    //공용
    private String user_id;
    private String select_date;

    //알콜
    private double alcohol_content;

    //수면
    private int sleeping_seconds;

    public MainHealthVO(){}

    public MainHealthVO(double sum_calorie, double sum_cardio_used_calorie, double sum_weight_used_calorie,
                        String user_id, String select_date, double alcohol_content, int sleeping_seconds) {
        this.sum_calorie = sum_calorie;
        this.sum_cardio_used_calorie = sum_cardio_used_calorie;
        this.sum_weight_used_calorie = sum_weight_used_calorie;
        this.user_id = user_id;
        this.select_date = select_date;
        this.alcohol_content = alcohol_content;
        this.sleeping_seconds = sleeping_seconds;
    }

    public double getSum_calorie() {
        return sum_calorie;
    }

    public void setSum_calorie(double sum_calorie) {
        this.sum_calorie = sum_calorie;
    }

    public double getSum_cardio_used_calorie() {
        return sum_cardio_used_calorie;
    }

    public void setSum_cardio_used_calorie(double sum_cardio_used_calorie) {
        this.sum_cardio_used_calorie = sum_cardio_used_calorie;
    }

    public double getSum_weight_used_calorie() {
        return sum_weight_used_calorie;
    }

    public void setSum_weight_used_calorie(double sum_weight_used_calorie) {
        this.sum_weight_used_calorie = sum_weight_used_calorie;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSelect_date() {
        return select_date;
    }

    public void setSelect_date(String select_date) {
        this.select_date = select_date;
    }

    public double getAlcohol_content() {
        return alcohol_content;
    }

    public void setAlcohol_content(double alcohol_content) {
        this.alcohol_content = alcohol_content;
    }

    public int getSleeping_seconds() {
        return sleeping_seconds;
    }

    public void setSleeping_seconds(int sleeping_seconds) {
        this.sleeping_seconds = sleeping_seconds;
    }
}
