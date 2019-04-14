package com.ardovic.githubgistviewer.data.gist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Owner implements Parcelable {

    @Expose
    private String login;
    @Expose
    @SerializedName("avatar_url")
    private String avatarUrl;

    private int rank;

    private Owner(Parcel in) {
        login = in.readString();
        avatarUrl = in.readString();
    }

    public static final Creator<Owner> CREATOR = new Creator<Owner>() {
        @Override
        public Owner createFromParcel(Parcel in) {
            return new Owner(in);
        }

        @Override
        public Owner[] newArray(int size) {
            return new Owner[size];
        }
    };

    public int getRank() {
        return rank;
    }

    public void incrementRank() {
        this.rank++;
    }

    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(login);
        dest.writeString(avatarUrl);
    }
}