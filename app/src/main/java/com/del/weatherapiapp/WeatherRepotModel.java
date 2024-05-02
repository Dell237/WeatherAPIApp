package com.del.weatherapiapp;

public class WeatherRepotModel {

    private int id;
    private  String main;
    private  String description;


    private  String datum;

    public WeatherRepotModel(int id, String main, String description, String datum) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.datum = datum;
    }

    public WeatherRepotModel() {

    }

    @Override
    public String toString() {
        return "Weather 3 hour: " + '\n' +"datum: "+  datum + '\n' +
                " main: " + main   +
                ", description: " + description  ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

}
