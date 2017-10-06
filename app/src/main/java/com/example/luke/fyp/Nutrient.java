package com.example.luke.fyp;

/**
 * Created by Luke on 04/10/2017.
 */

public class Nutrient {
    private String name;
    private String unit;
    private String value;

    public Nutrient(){

    }

    public Nutrient(String name, String unit, String value){
        this.name = name;
        this.unit = unit;
        this.value = value;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getUnit(){return unit;}

    public void setUnit(String unit) {this.unit = unit;}

    public String getValue() {return value;}

    public void setValue(String value) {this.value = value;}

}
