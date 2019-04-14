package com.ardovic.githubgistviewer.ui.detailed;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ardovic.githubgistviewer.App;
import com.ardovic.githubgistviewer.R;
import com.ardovic.githubgistviewer.data.Commit;
import com.ardovic.githubgistviewer.data.gist.Gist;
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

public class DetailedFragment extends Fragment implements MVPContract.IView {

    public static final String TAG = DetailedFragment.class.getSimpleName();

    private static final String GIST = "GIST";
    private static final String POSITION = "POSITION";

    @BindView(R.id.avatarIV)
    ImageView avatar;
    @BindView(R.id.titleTV)
    TextView title;
    @BindView(R.id.authorTV)
    TextView author;
    @BindView(R.id.contentsContainerLL)
    LinearLayout contentsContainer;
    @BindView(R.id.contentsTV)
    TextView contents;
    @BindView(R.id.commitsListRV)
    RecyclerView recycler;
    @BindView(R.id.loadingPB)
    ProgressBar progress;

    private Parcelable recyclerState;

    private MVPContract.IPresenter presenter;

    public static DetailedFragment newInstance(@NonNull Gist gist) {
        DetailedFragment fragment = new DetailedFragment();
        Bundle args = new Bundle();
        args.putParcelable(GIST, gist);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailed, container, false);
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
            presenter = new Presenter(this, getArguments().getParcelable(GIST));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(POSITION, recycler.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void initViews(Gist gist) {
        if (!TextUtils.isEmpty(gist.getDescription()))
            title.setText(gist.getDescription());
        String byAuthor = "by " + gist.getOwner().getLogin();
        author.setText(byAuthor);
        Picasso.with(App.getInstance().getApplicationContext())
                .load(gist.getOwner().getAvatarUrl())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(avatar, new Callback() {
                    @Override
                    public void onSuccess() {
                        // do nothing
                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(App.getInstance().getApplicationContext())
                                .load(gist.getOwner().getAvatarUrl())
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
        if (!gist.getFileNames().isEmpty()) {
            contentsContainer.setVisibility(View.VISIBLE);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < gist.getFileNames().size(); i++) {
                sb.append(gist.getFileNames().get(i));
                if (i != gist.getFileNames().size() - 1)
                    sb.append("\n");
            }
            contents.setText(sb.toString());
        }
    }

    @Override
    public void populateList(List<Commit> commits) {
        DetailedAdapter adapter = new DetailedAdapter(commits);
        recycler.setAdapter(adapter);
        recycler.getLayoutManager().onRestoreInstanceState(recyclerState);
        hideProgressBar();
    }

    @Override
    public void hideProgressBar() {
        progress.setVisibility(View.GONE);
    }
}
