package com.example.networkdemo.model;

public class WeatherRealtime {
    private String temperature;
    private String humidity;
    private String info;
    private String direct;
    private String power;
    private String aqi;

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDirect() {
        return direct;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public WeatherRealtime(){

    }
    public WeatherRealtime(String temperature, String humidity, String info, String direct, String power, String aqi) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.info = info;
        this.direct = direct;
        this.power = power;
        this.aqi = aqi;
    }

    @Override
    public String toString() {
        return "实时天气" +temperature +"度,湿度" + humidity + "%," + info +
                "," + direct + power + ",空气质量" + aqi;
    }
}
