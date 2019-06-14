package com.example.controlbt;

public class map {
    private Double lat;
    private Double log;
    public void setLat(Double lat){
        this.lat=lat;
    }

    public map(String data) {
        String[] Params = data.split(",");
        this.lat = Double.valueOf(Params[0]);
        this.log = Double.valueOf(Params[1]);
    }

    public Double getLat(){
        return lat;
    }
    public void setLog(Double log){
        this.log=log;
    }

    public Double getLog(){
        return log;
    }
}
