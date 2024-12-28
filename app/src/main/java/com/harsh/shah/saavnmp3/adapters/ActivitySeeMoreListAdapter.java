package com.harsh.shah.saavnmp3.adapters;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harsh.shah.saavnmp3.R;
import com.harsh.shah.saavnmp3.records.SongResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ActivitySeeMoreListAdapter extends RecyclerView.Adapter<ActivitySeeMoreListAdapter.ViewHolder> {

    private final List<SongResponse.Song> data;
    private final ActivitySeeMoreListAdapter.Mode mode;
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;

    public ActivitySeeMoreListAdapter(List<SongResponse.Song> data, ActivitySeeMoreListAdapter.Mode mode) {
        this.data = data;
        this.mode = mode;
    }

    public ActivitySeeMoreListAdapter(Mode mode){
        this.data = new ArrayList<>();
        this.mode = mode;
    }

    @NonNull
    @Override
    public ActivitySeeMoreListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View _v = View.inflate(parent.getContext(), viewType == 1 ? R.layout.activity_artist_profile_view_top_songs_item : R.layout.progress_bar_layout, null);
        _v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ActivitySeeMoreListAdapter.ViewHolder(_v);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivitySeeMoreListAdapter.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            //((ShimmerFrameLayout) holder.itemView.findViewById(R.id.shimmer)).startShimmer();
            return;
        }

        ImageView coverImage = holder.itemView.findViewById(R.id.coverImage);
        TextView coverTitle = holder.itemView.findViewById(R.id.coverTitle);
        TextView coverPlayed = holder.itemView.findViewById(R.id.coverPlayed);
        TextView positionTextView = holder.itemView.findViewById(R.id.position);
        ImageView moreImage = holder.itemView.findViewById(R.id.more);

        positionTextView.setText(String.valueOf(position + 1));
        coverTitle.setText(data.get(position).name());
        coverPlayed.setText(
                String.format("%s | %s", data.get(position).year(), data.get(position).label())
        );
        Picasso.get().load(Uri.parse(data.get(position).image().get(data.get(position).image().size() - 1).url())).into(coverImage);

//        holder.itemView.setOnClickListener(view -> {
//            if(mode == Mode.TOP_SONGS)
//                view.getContext().startActivity(new Intent(view.getContext(), MusicOverviewActivity.class).putExtra("id", data.get(position).id()));
//            else if(mode == Mode.TOP_ALBUMS) {
//                AlbumItem albumItem = new AlbumItem(
//                        data.get(position).id(),
//                        data.get(position).name(),
//                        data.get(position).image().get(data.get(position).image().size() - 1).url(),
//                        data.get(position).id()
//                );
//                holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), ListActivity.class)
//                        .putExtra("data", new Gson().toJson(albumItem))
//                        .putExtra("type", "album")
//                        .putExtra("id", data.get(position).id()));
//            }else if(mode == Mode.TOP_SINGLES){
//                view.getContext().startActivity(new Intent(view.getContext(), MusicOverviewActivity.class).putExtra("id", data.get(position).id()));
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return data == null?0:data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    public void add(SongResponse.Song da) {
        data.add(da);
        notifyItemInserted(data.size() - 1);
    }

    public void addAll(List<SongResponse.Song> moveResults) {
        for (SongResponse.Song result : moveResults) {
            add(result);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public enum Mode{
        TOP_SONGS,
        TOP_ALBUMS,
        TOP_SINGLES
    }
}
