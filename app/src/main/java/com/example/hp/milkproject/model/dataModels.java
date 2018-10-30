package com.example.hp.milkproject.model;

public class dataModels {

    String comment, date, fname, sname, uid, message, fullNames;
    int cowsMilked, litersCollected, totalCowsMilked, totalLitersCollected;
    boolean mala, yoghurt, milk;
    String code, birth, breed, kg, name, quantity, vacName, feedName, milkedTime, cost;

    public dataModels() {
    }

    public dataModels(String comment, String date, String fname, String sname, String uid, String message, String fullNames, int cowsMilked, int litersCollected, int totalCowsMilked, int totalLitersCollected, boolean mala, boolean yoghurt, boolean milk, String code, String birth, String breed, String kg, String name, String quantity, String vacName, String feedName, String milkedTime, String cost) {
        this.comment = comment;
        this.date = date;
        this.fname = fname;
        this.sname = sname;
        this.uid = uid;
        this.message = message;
        this.fullNames = fullNames;
        this.cowsMilked = cowsMilked;
        this.litersCollected = litersCollected;
        this.totalCowsMilked = totalCowsMilked;
        this.totalLitersCollected = totalLitersCollected;
        this.mala = mala;
        this.yoghurt = yoghurt;
        this.milk = milk;
        this.code = code;
        this.birth = birth;
        this.breed = breed;
        this.kg = kg;
        this.name = name;
        this.quantity = quantity;
        this.vacName = vacName;
        this.feedName = feedName;
        this.milkedTime = milkedTime;
        this.cost = cost;
    }

    public String getFullNames() {
        return fullNames;
    }

    public void setFullNames(String fullNames) {
        this.fullNames = fullNames;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getTotalCowsMilked() {
        return totalCowsMilked;
    }

    public void setTotalCowsMilked(int totalCowsMilked) {
        this.totalCowsMilked = totalCowsMilked;
    }

    public int getTotalLitersCollected() {
        return totalLitersCollected;
    }

    public void setTotalLitersCollected(int totalLitersCollected) {
        this.totalLitersCollected = totalLitersCollected;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCowsMilked() {
        return cowsMilked;
    }

    public void setCowsMilked(int cowsMilked) {
        this.cowsMilked = cowsMilked;
    }

    public int getLitersCollected() {
        return litersCollected;
    }

    public void setLitersCollected(int litersCollected) {
        this.litersCollected = litersCollected;
    }

    public boolean isMala() {
        return mala;
    }

    public boolean isYoghurt() {
        return yoghurt;
    }

    public boolean isMilk() {
        return milk;
    }

    public String getCode() {
        return code;
    }

    public String getBirth() {
        return birth;
    }

    public String getBreed() {
        return breed;
    }

    public String getKg() {
        return kg;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getVacName() {
        return vacName;
    }

    public String getFeedName() {
        return feedName;
    }

    public String getMilkedTime() {
        return milkedTime;
    }

    public String getCost() {
        return cost;
    }
}
