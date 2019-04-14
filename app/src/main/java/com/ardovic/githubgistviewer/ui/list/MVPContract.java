package com.ardovic.githubgistviewer.ui.list;

import com.ardovic.githubgistviewer.data.gist.Gist;
import com.ardovic.githubgistviewer.data.gist.Owner;

import java.util.List;

interface MVPContract {

    interface IView {
        void populateList(List<Gist> gists);

        void hideProgressBar();
    }

    interface IPresenter {
        void onGistClicked(Gist gist);

        void onUserClicked(Owner user);

        void onRefresh();
    }

    interface IModel {
        // not needed
    }
}

