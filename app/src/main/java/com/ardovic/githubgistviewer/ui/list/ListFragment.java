package com.ardovic.githubgistviewer.ui.list;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ardovic.githubgistviewer.R;
import com.ardovic.githubgistviewer.data.gist.Gist;
import com.ardovic.githubgistviewer.data.gist.Owner;
import com.ardovic.githubgistviewer.ui.FragmentCommunicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment implements MVPContract.IView, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = ListFragment.class.getSimpleName();

    private static final String POSITION_USERS = "POSITION_USERS";
    private static final String POSITION_GISTS = "POSITION_GISTS";

    @BindView(R.id.swipeSRL)
    SwipeRefreshLayout swipe;
    @BindView(R.id.topUsersTitleTV)
    TextView topUsersTitle;
    @BindView(R.id.topUsersListRV)
    RecyclerView recyclerUsers;
    @BindView(R.id.recentGistsTitleTV)
    TextView recentGistsTitle;
    @BindView(R.id.gistListRV)
    RecyclerView recyclerGists;
    @BindView(R.id.loadingPB)
    ProgressBar progress;

    private Parcelable recyclerUsersState;
    private Parcelable recyclerGistsState;

    private MVPContract.IPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipe.setOnRefreshListener(this);
        if (savedInstanceState != null) {
            recyclerUsersState = savedInstanceState.getParcelable(POSITION_USERS);
            recyclerGistsState = savedInstanceState.getParcelable(POSITION_GISTS);
        }
        recyclerUsers.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.HORIZONTAL, false));
        recyclerUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerUsers.setOnTouchListener((v, event) -> {
            swipe.setEnabled(false);
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    swipe.setEnabled(true);
                    break;
            }
            return false;
        });
        recyclerGists.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerGists.setItemAnimator(new DefaultItemAnimator());
        presenter = new Presenter(this, (FragmentCommunicator) getActivity());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(POSITION_USERS, recyclerUsers.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(POSITION_GISTS, recyclerGists.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void populateList(List<Gist> gists) {
        topUsersTitle.setVisibility(View.VISIBLE);
        recentGistsTitle.setVisibility(View.VISIBLE);
        GistListAdapter gistsAdapter = new GistListAdapter(gist -> presenter.onGistClicked(gist), gists);
        recyclerGists.setAdapter(gistsAdapter);
        recyclerGists.getLayoutManager().onRestoreInstanceState(recyclerGistsState);
        UserListAdapter usersAdapter = new UserListAdapter(user -> presenter.onUserClicked(user), getTopTenUsers(gists));
        recyclerUsers.setAdapter(usersAdapter);
        recyclerUsers.getLayoutManager().onRestoreInstanceState(recyclerUsersState);
        hideProgressBar();
    }

    private List<Owner> getTopTenUsers(List<Gist> gists) {
        List<Owner> users = new ArrayList<>();
        for (Gist gist : gists) {
            boolean newUser = true;
            for (int i = 0; i < users.size(); i++)
                if (users.get(i).getLogin().equals(gist.getOwner().getLogin())) {
                    users.get(i).incrementRank();
                    newUser = false;
                    break;
                }
            if (newUser)
                users.add(gist.getOwner());
        }
        Collections.sort(users, (Owner o1, Owner o2) -> o2.getRank() - o1.getRank());
        if (users.size() > 10)
            users.subList(10, users.size()).clear();
        return users;
    }

    @Override
    public void hideProgressBar() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        presenter.onRefresh();
        swipe.setRefreshing(false);
    }

    interface GistsListener {
        void onGistClicked(Gist gist);
    }

    interface UsersListener {
        void onUserClicked(Owner user);
    }
}
