package com.ardovic.githubgistviewer.ui;

import com.ardovic.githubgistviewer.data.gist.Gist;
import com.ardovic.githubgistviewer.data.gist.Owner;

public interface FragmentCommunicator {

    void showDetailedGist(Gist gist);

    void showUserPage(Owner user);
}
