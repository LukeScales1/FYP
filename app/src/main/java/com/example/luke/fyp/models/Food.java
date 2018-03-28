package com.example.luke.fyp.models;

/**
 * Created by Luke on 29/09/2017.
 */

public class Food {

    private String group;
    private String name;
    private String ndbno;

    public Food(){

    }

    public Food(String group, String name, String ndbno){
        this.group = group;
        this.name = name;
        this.ndbno = ndbno;

    }

    public String getGroup(){return group;}

    public void setGroup(String group) {this.group = group;}

    public String getName(){return name;}

    public void setName(String name) {this.name = name;}

    public String getNdbno() {return ndbno;}

    public void setNdbno(String ndbno) {this.ndbno = ndbno;}

}
