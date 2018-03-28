package com.example.luke.fyp.models;

/**
 * Created by Luke on 04/10/2017.
 */

public class Nutrient {
    private String name;
    private String unit;
    private String value;

    public Nutrient(){

    }

    public Nutrient(String name, String value, String unit){
        this.name = name;
        this.value = value;
        this.unit = unit;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getUnit(){return unit;}

    public void setUnit(String unit) {this.unit = unit;}

    public String getValue() {return value;}

    public void setValue(String value) {this.value = value;}

}
