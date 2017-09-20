package model;

public class WeatherTO {

    private int id, zipCode;
    private String type, degree;

    public WeatherTO(int id, int zipCode, String type, String degree) {
        this.id = id;
        this.zipCode = zipCode;
        this.type = type;
        this.degree = degree;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
