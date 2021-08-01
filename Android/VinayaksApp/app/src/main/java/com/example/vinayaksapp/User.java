package com.example.vinayaksapp;

public class User {

    String name;
    String phoneNo;
    String emailId;
    String age;
    String gender;
    String imageUri;
    String videoUri;


    public User() {
    }

    public User(String name, String phoneNo, String emailid, String age, String gender, String imageUri, String videoUri) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.emailId = emailid;
        this.age = age;
        this.gender = gender;
        this.imageUri = imageUri;
        this.videoUri = videoUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }
}
