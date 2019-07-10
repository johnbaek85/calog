package com.example.calog.Sleeping.DecibelCheck;

/**
 * Created by bodekjan on 2016/8/8.
 */
public class World {
    public static float dbCount = 40;
    public static float minDB =100;
    public static float maxDB =0;
    public static float lastDbCount = dbCount;
    private static float min = 0.5f;  //Set the minimum sound change
    private static float value = 0;   // Sound decibel value
    public static void setDbCount(float dbValue) {
        if (dbValue > lastDbCount) {
            value = dbValue - lastDbCount > min ? dbValue - lastDbCount : min;
        }else{
            value = dbValue - lastDbCount < -min ? dbValue - lastDbCount : -min;
        }
        dbCount = lastDbCount + value * 0.2f ; //소리를 너무 빠르게 하지 않기 위한 식
        lastDbCount = dbCount;
        if(dbCount<minDB) minDB=dbCount;
        if(dbCount>maxDB) maxDB=dbCount;
    }

}