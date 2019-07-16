package com.example.calog.VO;

import android.os.Parcel;
import android.os.Parcelable;

public class DietMenuVO implements Parcelable {

    private int diet_menu_id;
    private String diet_menu_name;
    private int calorie;
    private boolean isChecked;

    protected DietMenuVO(Parcel in) {
        diet_menu_id = in.readInt();
        diet_menu_name = in.readString();
        calorie = in.readInt();
        isChecked = in.readByte() != 0;
    }

    public static final Creator<DietMenuVO> CREATOR = new Creator<DietMenuVO>() {
        @Override
        public DietMenuVO createFromParcel(Parcel in) {
            return new DietMenuVO(in);
        }

        @Override
        public DietMenuVO[] newArray(int size) {
            return new DietMenuVO[size];
        }
    };

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public DietMenuVO() {
    }

    public DietMenuVO(String diet_menu_name, int calorie) {
        this.diet_menu_name = diet_menu_name;
        this.calorie = calorie;
    }

    public int getDiet_menu_id() {
        return diet_menu_id;
    }

    public void setDiet_menu_id(int diet_menu_id) {
        this.diet_menu_id = diet_menu_id;
    }

    public String getDiet_menu_name() {
        return diet_menu_name;
    }

    public void setDiet_menu_name(String diet_menu_name) {
        this.diet_menu_name = diet_menu_name;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    @Override
    public String toString() {
        return "DietMenuVO{" +
                "diet_menu_id=" + diet_menu_id +
                ", diet_menu_name='" + diet_menu_name + '\'' +
                ", calorie=" + calorie +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(diet_menu_name);
        parcel.writeInt(diet_menu_id);
        parcel.writeInt(calorie);
    }
}
