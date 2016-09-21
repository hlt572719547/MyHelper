package com.example.helper;

public class UserInfo {

    private String name;
    private String number;
    private String sortFirstLetter;

    public UserInfo(String name, String number) {
        super();
        this.name = name;
        this.number = number;
    }

    
    public UserInfo(String name, String number, String sortFirstLetter) {
        super();
        this.name = name;
        this.number = number;
        this.sortFirstLetter = sortFirstLetter;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSortFirstLetter() {
        return sortFirstLetter;
    }

    public void setSortFirstLetter(String sortFirstLetter) {
        this.sortFirstLetter = sortFirstLetter;
    }
}
