package com.example.bloodbank;

public class donor_vreq_listitem {

    private String name;
    private String age;
    private String sex;

    public donor_vreq_listitem(String name, String age, String sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }
}
