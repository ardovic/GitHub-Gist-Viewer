package com.ardovic.githubgistviewer.ui.user;

import com.ardovic.githubgistviewer.data.gist.Gist;
import com.ardovic.githubgistviewer.data.gist.Owner;

import java.util.List;

interface MVPContract {

    interface IView {
        void initViews(Owner owner);

        void populateList(List<Gist> gists);

        void hideProgressBar();
    }

    interface IPresenter {
        void onGistClicked(Gist gist);
    }

    interface IModel {
        // not needed
    }

}
