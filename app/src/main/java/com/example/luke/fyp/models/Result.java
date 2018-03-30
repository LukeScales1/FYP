package com.example.luke.fyp.models;

/**
 * Created by Luke on 05/10/2017.
 */

public class Result {

        private String ndbno;
        private String name;
        private String calories;
        private String fat;
        private String sats;
        private String carbs;
        private String sugars;
        private String protein;
        private String fibre;
        private String sodium;

        public Result(){

        }

        public Result(String ndbno, String name, String calories, String fat, String sats, String carbs, String sugars, String protein, String fibre, String sodium){
            this.ndbno = ndbno;
            this.name = name;
            this.calories = calories;
            this.fat = fat;
            this.sats = sats;
            this.carbs = carbs;
            this.sugars = sugars;
            this.protein = protein;
            this.fibre = fibre;
            this.sodium = sodium;
        }

        public String getNdbno() {return ndbno;}

        public void setNdbno(String ndbno) {this.ndbno = ndbno;}

        public String getName(){return name;}

        public void setName(String name) {this.name = name;}

        public String getCalories() {return calories;}

        public void setCalories(String calories) {this.calories = calories;}

        public String getFat() {return fat;}

        public void setFat(String fat) {this.fat = fat;}

        public String getSats() {return sats;}

        public void setSats(String sats) {this.sats = sats;}

        public String getCarbs() {return carbs;}

        public void setCarbs(String carbs) {this.carbs = carbs;}

        public String getSugars() {return sugars;}

        public void setSugars(String sugars) {this.sugars = sugars;}

        public String getProtein() {return protein;}

        public void setProtein(String protein) {this.protein = protein;}

        public String getFibre() {return fibre;}

        public void setFibre(String fibre) {this.fibre = fibre;}

        public String getSodium() {return sodium;}

        public void setSodium(String sodium) {this.sodium = sodium;}

}
