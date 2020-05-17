package com.example.saathealth;

public class UserDatabase {
    private String Name;
    private String Phone;
    private String Language;
    private String Location;

    public UserDatabase(String name, String phone, String language, String location) {
        Name = name;
        Phone = phone;
        Language = language;
        Location = location;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
