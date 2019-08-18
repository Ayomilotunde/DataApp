package com.example.personal.Model;

public class Data {

    private String name;
    private String matricno;
    private String description;
    private String id;
    private String date;

    public  Data() {

    }

    public Data(String name, String matricno, String description, String id, String date) {
        this.name = name;
        this.matricno = matricno;
        this.description = description;
        this.id = id;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatricno() {
        return matricno;
    }

    public void setMatricno(String matricno) {
        this.matricno = matricno;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
