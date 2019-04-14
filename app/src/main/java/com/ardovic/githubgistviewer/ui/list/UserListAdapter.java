package com.ardovic.githubgistviewer.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ardovic.githubgistviewer.App;
import com.ardovic.githubgistviewer.R;
import com.ardovic.githubgistviewer.data.gist.Owner;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

class UserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ListFragment.UsersListener listener;
    private List<Owner> owners;

    UserListAdapter(ListFragment.UsersListener listener, List<Owner> owners) {
        this.listener = listener;
        this.owners = owners;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.bindView(owners.get(position));
    }

    @Override
    public int getItemCount() {
        return owners == null ? 0 : owners.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.avatarIV)
        ImageView avatar;
        @BindView(R.id.authorTV)
        TextView author;

        private Owner owner;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bindView(final Owner owner) {
            this.owner = owner;
            String combinedInfo = "(" + (owner.getRank() + 1) + ") " + owner.getLogin();
            author.setText(combinedInfo);
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
                            //Try again online if cache failed
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
        public void onClick(View v) {
            listener.onUserClicked(owner);
        }
    }
}
