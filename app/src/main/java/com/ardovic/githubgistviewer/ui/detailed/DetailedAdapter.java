package com.ardovic.githubgistviewer.ui.detailed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ardovic.githubgistviewer.R;
import com.ardovic.githubgistviewer.data.Commit;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

class DetailedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Commit> commits;

    DetailedAdapter(List<Commit> commits) {
        this.commits = commits;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.bindView(commits.get(position));
    }

    @Override
    public int getItemCount() {
        return commits == null ? 0 : commits.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.committedAtTV)
        TextView committedAt;
        @BindView(R.id.deletionsTV)
        TextView deletions;
        @BindView(R.id.additionsTV)
        TextView additions;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindView(Commit commit) {
            committedAt.setText(commit.getCommittedAt());
            deletions.setText(String.valueOf(commit.getChangeStatus().getDeletions()));
            additions.setText(String.valueOf(commit.getChangeStatus().getAdditions()));
        }
    }
}
