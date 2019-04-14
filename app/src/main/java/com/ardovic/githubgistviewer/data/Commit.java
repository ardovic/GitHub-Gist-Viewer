package com.ardovic.githubgistviewer.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Commit {

    @Expose
    @SerializedName("committed_at")
    private String committedAt;

    @Expose
    @SerializedName("change_status")
    private ChangeStatus changeStatus;

    public String getCommittedAt() {
        return committedAt;
    }

    public ChangeStatus getChangeStatus() {
        return changeStatus;
    }

    public class ChangeStatus {

        @Expose
        private int deletions;
        @Expose
        private int additions;

        public int getDeletions() {
            return deletions;
        }

        public int getAdditions() {
            return additions;
        }
    }
}
