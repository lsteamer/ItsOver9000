package com.example.android.itsover9000.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Getting the pertinent information.
 * 3 String parameters and a List Object
 */

public class Feed {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("unit")
    @Expose
    private String unit;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("values")
    @Expose
    private ArrayList<Values> values;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Values> getValues() {
        return values;
    }

    public void setValues(ArrayList<Values> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", description='" + description + '\'' +
                ", values=" + values +
                '}';
    }
}
