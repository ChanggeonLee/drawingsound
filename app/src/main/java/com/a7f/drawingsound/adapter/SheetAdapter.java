package com.a7f.drawingsound.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a7f.drawingsound.R;
import com.a7f.drawingsound.model.SheetT;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SheetAdapter extends RecyclerView.Adapter<SheetAdapter.ViewHolder> {

    private ArrayList<SheetT> items = new ArrayList<>();

    @NonNull
    @Override
    public SheetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SheetAdapter.ViewHolder viewHolder, int position) {

        SheetT item = items.get(position);

        Glide.with(viewHolder.itemView.getContext())
                .load(item.getUrl())
                .into(viewHolder.ivMovie);

        viewHolder.tvTitle.setText(item.getTitle());
        viewHolder.tvContent.setText(item.getContent());
        viewHolder.tvGenre.setText(item.getGenre());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<SheetT> items) {
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivMovie;
        TextView tvTitle, tvContent, tvGenre;

        ViewHolder(View itemView) {
            super(itemView);

            ivMovie = itemView.findViewById(R.id.iv_item_movie);

            tvTitle = itemView.findViewById(R.id.tv_item_movie_title);
            tvContent = itemView.findViewById(R.id.tv_item_movie_content);
            tvGenre = itemView.findViewById(R.id.tv_item_movie_genre);
        }
    }
}
