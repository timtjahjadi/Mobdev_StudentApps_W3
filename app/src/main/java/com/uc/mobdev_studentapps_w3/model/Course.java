package com.uc.mobdev_studentapps_w3.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Course implements Parcelable {
    private String id, subject, day, lecturerId;
    private Integer timeStart, timeEnd;

    Course() {}

    public Course(String id, String subject, String day, String lecturerId, Integer timeStart, Integer timeEnd) {
        this.id = id;
        this.subject = subject;
        this.day = day;
        this.lecturerId = lecturerId;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    protected Course(Parcel in) {
        id = in.readString();
        subject = in.readString();
        day = in.readString();
        lecturerId = in.readString();
        if (in.readByte() == 0) {
            timeStart = null;
        } else {
            timeStart = in.readInt();
        }
        if (in.readByte() == 0) {
            timeEnd = null;
        } else {
            timeEnd = in.readInt();
        }
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getDay() {
        return day;
    }

    public String getLecturerId() {
        return lecturerId;
    }

    public Integer getTimeStart() {
        return timeStart;
    }

    public Integer getTimeEnd() {
        return timeEnd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(subject);
        dest.writeString(day);
        dest.writeString(lecturerId);
        if (timeStart == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(timeStart);
        }
        if (timeEnd == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(timeEnd);
        }
    }
}
