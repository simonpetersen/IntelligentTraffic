package model.dto;

public class WeatherTO {

    private int weatherDataId, zipCode;
    private String type, degree;

    public WeatherTO(int weatherDataId, int zipCode, String type, String degree) {
        this.weatherDataId = weatherDataId;
        this.zipCode = zipCode;
        this.type = type;
        this.degree = degree;
    }

    public int getWeatherDataId() {
        return weatherDataId;
    }

    public void setWeatherDataId(int weatherDataId) {
        this.weatherDataId = weatherDataId;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
}
