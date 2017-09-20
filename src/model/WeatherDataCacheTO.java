package model;

import java.util.Date;

public class WeatherDataCacheTO {

    private int id;
    private Date date;
    private String xml;

    public WeatherDataCacheTO(int id, Date date, String xml) {
        this.id = id;
        this.date = date;
        this.xml = xml;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
}
