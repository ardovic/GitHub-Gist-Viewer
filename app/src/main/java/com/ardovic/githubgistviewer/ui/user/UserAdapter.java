package com.ardovic.githubgistviewer.ui.user;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ardovic.githubgistviewer.R;
import com.ardovic.githubgistviewer.data.Commit;
import com.ardovic.githubgistviewer.data.gist.Gist;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private UserFragment.Listener listener;
    private List<Gist> gists;

    UserAdapter(UserFragment.Listener listener, List<Gist> gists) {
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

        void bindView(Gist gist) {
            this.gist = gist;
            avatar.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(gist.getDescription()))
                title.setText(gist.getDescription());
            author.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            listener.onGistClicked(gist);
        }
    }
}
