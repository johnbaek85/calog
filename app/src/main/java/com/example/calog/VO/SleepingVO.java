package com.example.calog.VO;

import java.sql.Date;

public class SleepingVO {

    private String userId;
    private Date sleepingDate;
    private int sleepingSeconds;
    private int snoringSeconds;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getSleepingDate() {
        return sleepingDate;
    }

    public void setSleepingDate(Date sleepingDate) {
        this.sleepingDate = sleepingDate;
    }

    public int getSleepingSeconds() {
        return sleepingSeconds;
    }

    public void setSleepingSeconds(int sleepingSeconds) {
        this.sleepingSeconds = sleepingSeconds;
    }

    public int getSnoringSeconds() {
        return snoringSeconds;
    }

    public void setSnoringSeconds(int snoringSeconds) {
        this.snoringSeconds = snoringSeconds;
    }

    @Override
    public String toString() {
        return "SleepingVO{" +
                "userId='" + userId + '\'' +
                ", sleepingDate=" + sleepingDate +
                ", sleepingSeconds=" + sleepingSeconds +
                ", snoringSeconds=" + snoringSeconds +
                '}';
    }
}
