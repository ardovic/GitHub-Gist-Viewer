package com.ardovic.githubgistviewer.ui.list;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ardovic.githubgistviewer.App;
import com.ardovic.githubgistviewer.R;
import com.ardovic.githubgistviewer.data.gist.Gist;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

class GistListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ListFragment.GistsListener listener;
    private List<Gist> gists;

    GistListAdapter(ListFragment.GistsListener listener, List<Gist> gists) {
        this.listener = listener;
        this.gists = gists;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.bindView(gists.get(position));
    }

    @Override
    public int getItemCount() {
        return gists == null ? 0 : gists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.avatarIV)
        ImageView avatar;
        @BindView(R.id.titleTV)
        TextView title;
        @BindView(R.id.authorTV)
        TextView author;

        private Gist gist;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bindView(final Gist gist) {
            this.gist = gist;
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
        }

        @Override
        public void onClick(View v) {
            listener.onGistClicked(gist);
        }
    }
}
