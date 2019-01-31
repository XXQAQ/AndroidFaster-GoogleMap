package com.xq.androidfaster_map.bean.entity;

import android.os.Parcel;
import android.os.Parcelable;
import com.xq.androidfaster.bean.behavior.TitleBehavior;
import com.xq.androidfaster_map.bean.behavior.MarkBehavior;
import java.io.Serializable;

public class MarkBean implements MarkBehavior,TitleBehavior {

    private double latitude;
    private double longitude;
    private String title;
    private Object tag;

    public MarkBean() {
    }

    public MarkBean(double latitude, double longitude, String title) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
    }

    public MarkBean(double latitude, double longitude, String title, Object tag) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "MarkBean{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", title='" + title + '\'' +
                ", tag=" + tag +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MarkBean markBean = (MarkBean) o;

        if (Double.compare(markBean.latitude, latitude) != 0) return false;
        if (Double.compare(markBean.longitude, longitude) != 0) return false;
        if (title != null ? !title.equals(markBean.title) : markBean.title != null) return false;
        return tag != null ? tag.equals(markBean.tag) : markBean.tag == null;
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
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        return result;
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

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.title);
        if (tag instanceof Parcelable)
            dest.writeParcelable((Parcelable) tag, flags);
        else    if (tag instanceof Serializable)
            dest.writeSerializable((Serializable) tag);
    }

    protected MarkBean(Parcel in) {
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.title = in.readString();
        if (tag instanceof Parcelable)
            this.tag = in.readParcelable(Object.class.getClassLoader());
        else    if (tag instanceof Serializable)
            this.tag = in.readSerializable();
    }

    public static final Creator<MarkBean> CREATOR = new Creator<MarkBean>() {
        @Override
        public MarkBean createFromParcel(Parcel source) {
            return new MarkBean(source);
        }

        @Override
        public MarkBean[] newArray(int size) {
            return new MarkBean[size];
        }
    };
}
