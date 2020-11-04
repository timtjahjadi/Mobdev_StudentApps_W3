package com.uc.mobdev_studentapps_w3.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CourseStudent implements Parcelable {

    private String id, studentId, courseId;

    CourseStudent(){}

    public CourseStudent(String id, String studentId, String courseId) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
    }

    protected CourseStudent(Parcel in) {
        id = in.readString();
        studentId = in.readString();
        courseId = in.readString();
    }

    public static final Creator<CourseStudent> CREATOR = new Creator<CourseStudent>() {
        @Override
        public CourseStudent createFromParcel(Parcel in) {
            return new CourseStudent(in);
        }

        @Override
        public CourseStudent[] newArray(int size) {
            return new CourseStudent[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(studentId);
        dest.writeString(courseId);
    }
}
