package com.ardovic.githubgistviewer.ui.user;

import com.ardovic.githubgistviewer.data.gist.Gist;
import com.ardovic.githubgistviewer.data.gist.Owner;
import com.ardovic.githubgistviewer.network.ApiClient;
import com.ardovic.githubgistviewer.network.ApiInterface;
import com.ardovic.githubgistviewer.ui.FragmentCommunicator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class Presenter implements MVPContract.IPresenter, Callback<List<Gist>> {

    private MVPContract.IView view;

    private FragmentCommunicator communicator;

    Presenter(MVPContract.IView view, FragmentCommunicator communicator, Owner owner) {
        this.view = view;
        this.communicator = communicator;
        view.initViews(owner);
        updateList(owner.getLogin());
    }

    private void updateList(String userName) {
        ApiClient.getApiClient()
                .create(ApiInterface.class)
                .getUsersAllPublicGists(userName)
                .enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Gist>> call, Response<List<Gist>> response) {
        if (response.body() != null) {
            view.populateList(response.body());
        } else {
            // do nothing
        }
    }

    @Override
    public void onFailure(Call<List<Gist>> call, Throwable t) {
        // do nothing
    }

    @Override
    public void onGistClicked(Gist gist) {
        communicator.showDetailedGist(gist);
    }
}
