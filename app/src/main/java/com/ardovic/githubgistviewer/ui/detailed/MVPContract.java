package com.ardovic.githubgistviewer.ui.detailed;

import com.ardovic.githubgistviewer.data.Commit;
import com.ardovic.githubgistviewer.data.gist.Gist;

import java.util.List;

interface MVPContract {

    interface IView {
        void initViews(Gist gist);

        void populateList(List<Commit> commits);

        void hideProgressBar();
    }

    interface IPresenter {
        // not needed
    }

    interface IModel {
        // not needed
    }

}
