package com.example.bloodbank;

public class donor_vreq_listitem {

    private String name;
    private String age;
    private String sex;
    private int id;

    public donor_vreq_listitem(int id, String name, String age, String sex) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public int getId () { return id; }

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
