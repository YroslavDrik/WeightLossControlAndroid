package com.example.weightlosscontrolandroid;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class Todo implements Parcelable {

    public String Name;
    public String UserNameDataBase;
    public String Password;
    public  double DesiredWeight;
    public List<Double> WeightChanges;
    public List<String> DateChanges;
    public Todo(){

    }
    public Todo(String Name, String UserNameDataBase, String Password , double DesiredWeight , List<Double> WeightChanges , List<String> DateChanges ) {
        this.Name = Name;
        this.UserNameDataBase = UserNameDataBase;
        this.Password = Password;
        this.DateChanges = DateChanges;
        this.DesiredWeight = DesiredWeight;
        this.WeightChanges = WeightChanges;
    }

    protected Todo(Parcel in) {
        Name = in.readString();
        UserNameDataBase = in.readString();
        Password = in.readString();
        DesiredWeight = in.readDouble();
        DateChanges = in.createStringArrayList();
    }

    public static final Creator<Todo> CREATOR = new Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(Name);
        parcel.writeString(UserNameDataBase);
        parcel.writeString(Password);
        parcel.writeDouble(DesiredWeight);
        parcel.writeStringList(DateChanges);
    }
}