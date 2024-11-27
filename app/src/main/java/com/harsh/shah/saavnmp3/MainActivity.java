package com.harsh.shah.saavnmp3;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        GridView gridView = findViewById(R.id.playlist_grid);
//        gridView.setAdapter(new CustomAdapter(this));
//        setGridViewHeightBasedOnChildren(gridView, 2);
        RecyclerView recyclerView = findViewById(R.id.playlist_recycler_view);
        int span = calculateNoOfColumns(this, 200);
        recyclerView.setLayoutManager(new GridLayoutManager(this,span));
        recyclerView.setAdapter(new PlaylistAdapter());
    }

    public class CustomAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        public CustomAdapter(Context applicationContext) {
            this.context = applicationContext;
            inflater = (LayoutInflater.from(applicationContext));
        }
        @Override
        public int getCount() {
            return 10;
        }
        @Override
        public Object getItem(int i) {
            return null;
        }
        @Override
        public long getItemId(int i) {
            return 0;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.activity_main_playlist_item, null); // inflate the layout
            return view;
        }
    }

    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if( items > columns ){
            x = (float) items/columns;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);

    }


    class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistAdapterViewHolder> {

        @NonNull
        @Override
        public PlaylistAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View _v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_playlist_item, null, false);
            _v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new PlaylistAdapterViewHolder(_v);
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        @Override
        public void onBindViewHolder(@NonNull PlaylistAdapterViewHolder holder, int position) {

        }

        class PlaylistAdapterViewHolder extends RecyclerView.ViewHolder {
            public PlaylistAdapterViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }

}