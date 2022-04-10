// Author Euan Millar, S1820947
package com.example.millar_euan_s1820947;

import android.os.Parcel;
import android.os.Parcelable;

public class incidentData implements Parcelable {

        private String title;
        private String description;
        private String point;
        private String pubDate;

    public incidentData()
        {
            title = "";
            description = "";
            point = "";
            pubDate = "";
        }

    public incidentData(String atitle,String adescription,String apoint, String apubDate)
        {
            title = atitle;
            description = adescription;
            point = apoint;
            pubDate = apubDate;
        }

    protected incidentData(Parcel in) {
        title = in.readString();
        description = in.readString();
        point = in.readString();
        pubDate = in.readString();
    }

    public static final Creator<incidentData> CREATOR = new Creator<incidentData>() {
        @Override
        public incidentData createFromParcel(Parcel in) {
            return new incidentData(in);
        }

        @Override
        public incidentData[] newArray(int size) {
            return new incidentData[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPoint() {
        return point;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public String toString() {
        return "incidentData{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", point='" + point + '\'' +
                ", pubDate='" + pubDate + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(point);
        parcel.writeString(pubDate);
    }
} // End of class


