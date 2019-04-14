package com.ardovic.githubgistviewer.ui.detailed;

import com.ardovic.githubgistviewer.data.Commit;
import com.ardovic.githubgistviewer.data.gist.Gist;
import com.ardovic.githubgistviewer.network.ApiClient;
import com.ardovic.githubgistviewer.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class Presenter implements MVPContract.IPresenter, Callback<List<Commit>> {

    private MVPContract.IView view;

    Presenter(MVPContract.IView view, Gist gist) {
        this.view = view;
        ApiClient.getApiClient()
                .create(ApiInterface.class)
                .getCommits(gist.getId())
                .enqueue(this);
        view.initViews(gist);
    }

    @Override
    public void onResponse(Call<List<Commit>> call, Response<List<Commit>> response) {
        if (response.body() != null) {
            view.populateList(response.body());
        } else {
            // do nothing
        }
    }

    @Override
    public void onFailure(Call<List<Commit>> call, Throwable t) {
        // do nothing
    }
}
