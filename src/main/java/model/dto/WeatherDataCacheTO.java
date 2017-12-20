package model.dto;

import java.util.Date;

public class WeatherDataCacheTO {

    private int yrId;
    private Date date;
    private String xml;

    public WeatherDataCacheTO(int yrId, Date date, String xml) {
        this.yrId = yrId;
        this.date = date;
        this.xml = xml;
    }

    public WeatherDataCacheTO(Date date, String xml) {
        this.date = date;
        this.xml = xml;
    }

    public int getYrId() {
        return yrId;
    }

    public void setYrId(int yrId) {
        this.yrId = yrId;
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
