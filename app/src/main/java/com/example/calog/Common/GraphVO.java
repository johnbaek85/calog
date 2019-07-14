package com.example.calog.Common;

public class GraphVO
{
    //그래프Fragment에 전달할 데이터 객체

    float data_float;
    String data_date;

    public GraphVO(){}

    public GraphVO(float data_float, String data_date)
    {
        this.data_float = data_float;
        this.data_date = data_date;
    }

    public float getData_float() {
        return data_float;
    }

    public void setData_float(float data_float) {
        this.data_float = data_float;
    }

    public String getData_date() {
        return data_date;
    }

    public void setData_date(String data_date) {
        this.data_date = data_date;
    }
}
