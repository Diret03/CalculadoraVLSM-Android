package com.diret.calculdora_vlsm;

import android.os.Parcel;
import android.os.Parcelable;

public class Host implements Parcelable {
    private String name;
    private int numHosts;

    // Constructor
    public Host(String name, int numHosts) {
        this.name = name;
        this.numHosts = numHosts;
    }

    // Parcelable constructor (required)
    protected Host(Parcel in) {
        name = in.readString();
        numHosts = in.readInt();
    }

    // Creator
    public static final Creator<Host> CREATOR = new Creator<Host>() {
        @Override
        public Host createFromParcel(Parcel in) {
            return new Host(in);
        }

        @Override
        public Host[] newArray(int size) {
            return new Host[size];
        }
    };

    // Getter for the host name
    public String getName() {
        return name;
    }

    // Setter for the host name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for the number of hosts
    public int getNumHosts() {
        return numHosts;
    }

    // Setter for the number of hosts
    public void setNumHosts(int numHosts) {
        this.numHosts = numHosts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(numHosts);
    }
}