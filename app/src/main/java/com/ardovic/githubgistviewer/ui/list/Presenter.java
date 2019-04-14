package com.ardovic.githubgistviewer.ui.list;

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

    Presenter(MVPContract.IView view, FragmentCommunicator communicator) {
        this.view = view;
        this.communicator = communicator;
        updateList();
    }

    private void updateList() {
        ApiClient.getApiClient()
                .create(ApiInterface.class)
                .getPublicGists()
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

    @Override
    public void onUserClicked(Owner user) {
        communicator.showUserPage(user);
    }

    @Override
    public void onRefresh() {
        updateList();
    }
}
