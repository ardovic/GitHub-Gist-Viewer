package com.ardovic.githubgistviewer.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ardovic.githubgistviewer.R;
import com.ardovic.githubgistviewer.data.gist.Gist;
import com.ardovic.githubgistviewer.data.gist.Owner;
import com.ardovic.githubgistviewer.ui.detailed.DetailedFragment;
import com.ardovic.githubgistviewer.ui.list.ListFragment;
import com.ardovic.githubgistviewer.ui.user.UserFragment;

public class MainActivity extends AppCompatActivity implements FragmentCommunicator {

    Fragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if ((listFragment = getSupportFragmentManager().findFragmentByTag(ListFragment.TAG)) != null)
            transaction.show(listFragment);
        else
            transaction.add(R.id.rootContainer, listFragment = new ListFragment(), ListFragment.TAG);
        transaction.commit();
    }

    @Override
    public void showDetailedGist(Gist gist) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.rootContainer, DetailedFragment.newInstance(gist))
                .addToBackStack(DetailedFragment.TAG)
                .commit();
    }

    @Override
    public void showUserPage(Owner user) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.rootContainer, UserFragment.newInstance(user))
                .addToBackStack(UserFragment.TAG)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
