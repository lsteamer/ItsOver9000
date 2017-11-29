package com.example.android.itsover9000.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * List Object that includes all of our x,y Data.
 */

public class Values {

    @SerializedName("x")
    @Expose
    private String x;

    @SerializedName("y")
    @Expose
    private String y;

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Values{" +
                "x='" + x + '\'' +
                ", y='" + y + '\'' +
                '}';
    }
}
