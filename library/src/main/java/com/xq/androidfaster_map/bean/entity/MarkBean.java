package com.xq.androidfaster_map.bean.entity;

import com.xq.androidfaster_map.bean.behavior.MarkBehavior;

public class MarkBean implements MarkBehavior {

    private double latitude;
    private double longitude;
    private String title;
    private String littleTitle;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MarkBean markBean = (MarkBean) o;

        if (Double.compare(markBean.latitude, latitude) != 0) return false;
        if (Double.compare(markBean.longitude, longitude) != 0) return false;
        if (title != null ? !title.equals(markBean.title) : markBean.title != null) return false;
        return littleTitle != null ? littleTitle.equals(markBean.littleTitle) : markBean.littleTitle == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (littleTitle != null ? littleTitle.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MarkBean{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", title='" + title + '\'' +
                ", littleTitle='" + littleTitle + '\'' +
                '}';
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getLittleTitle() {
        return littleTitle;
    }

    public void setLittleTitle(String littleTitle) {
        this.littleTitle = littleTitle;
    }
}
