package com.ardovic.githubgistviewer.ui.user;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ardovic.githubgistviewer.App;
import com.ardovic.githubgistviewer.R;
import com.ardovic.githubgistviewer.data.gist.Gist;
import com.ardovic.githubgistviewer.data.gist.Owner;
import com.ardovic.githubgistviewer.ui.FragmentCommunicator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserFragment extends Fragment implements MVPContract.IView {

    public static final String TAG = UserFragment.class.getSimpleName();

    private static final String OWNER = "OWNER";
    private static final String POSITION = "POSITION";

    @BindView(R.id.avatarIV)
    ImageView avatar;
    @BindView(R.id.authorTV)
    TextView author;
    @BindView(R.id.gistListRV)
    RecyclerView recycler;
    @BindView(R.id.loadingPB)
    ProgressBar progress;

    private Parcelable recyclerState;

    private MVPContract.IPresenter presenter;

    public static UserFragment newInstance(@NonNull Owner owner) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putParcelable(OWNER, owner);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null)
            recyclerState = savedInstanceState.getParcelable(POSITION);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recycler.setItemAnimator(new DefaultItemAnimator());
        if (getArguments() != null)
            presenter = new Presenter(this, (FragmentCommunicator) getActivity(), getArguments().getParcelable(OWNER));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(POSITION, recycler.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void initViews(Owner owner) {
        author.setText(owner.getLogin());
        Picasso.with(App.getInstance().getApplicationContext())
                .load(owner.getAvatarUrl())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(avatar, new Callback() {
                    @Override
                    public void onSuccess() {
                        // do nothing
                    }

                    @Override
                    public void onError() {
                        //Try again online if cache fails
                        Picasso.with(App.getInstance().getApplicationContext())
                                .load(owner.getAvatarUrl())
                                .into(avatar, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        // do nothing
                                    }

                                    @Override
                                    public void onError() {
                                        // do nothing
                                    }
                                });
                    }
                });
    }

    @Override
    public void populateList(List<Gist> gists) {
        UserAdapter adapter = new UserAdapter(gist -> presenter.onGistClicked(gist), gists);
        recycler.setAdapter(adapter);
        recycler.getLayoutManager().onRestoreInstanceState(recyclerState);
        hideProgressBar();
    }

    @Override
    public void hideProgressBar() {
        progress.setVisibility(View.GONE);
    }

    interface Listener {
        void onGistClicked(Gist gist);
    }
}
