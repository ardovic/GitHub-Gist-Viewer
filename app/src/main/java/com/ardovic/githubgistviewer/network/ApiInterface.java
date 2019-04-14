package com.ardovic.githubgistviewer.network;

import com.ardovic.githubgistviewer.data.Commit;
import com.ardovic.githubgistviewer.data.gist.Gist;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("/gists/public")
    Call<List<Gist>> getPublicGists(); // list of 30 by default

    @GET("/gists/{gistId}/commits")
    Call<List<Commit>> getCommits(@Path("gistId") String gistId);

    @GET("/users/{userName}/gists")
    Call<List<Gist>> getUsersAllPublicGists(@Path("userName") String userName);
}
