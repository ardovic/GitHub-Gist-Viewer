package com.ardovic.githubgistviewer.data.gist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Gist implements Parcelable {

    @Expose
    private String id;
    @Expose
    private String description;
    @Expose
    private Owner owner;
    @Expose
    @SerializedName("files")
    private JsonObject filesObject;

    protected Gist(Parcel in) {
        id = in.readString();
        description = in.readString();
        owner = in.readParcelable(Owner.class.getClassLoader());
    }

    public static final Creator<Gist> CREATOR = new Creator<Gist>() {
        @Override
        public Gist createFromParcel(Parcel in) {
            return new Gist(in);
        }

        @Override
        public Gist[] newArray(int size) {
            return new Gist[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Owner getOwner() {
        return owner;
    }

    public List<String> getFileNames() {
        List<String> files = new ArrayList<>();
        Set<Map.Entry<String, JsonElement>> entries = filesObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            files.add(entry.getKey());
        }
        return files;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
        dest.writeParcelable(owner, flags);
    }
}
