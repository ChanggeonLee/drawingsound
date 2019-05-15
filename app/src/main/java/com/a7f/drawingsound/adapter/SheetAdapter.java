package com.a7f.drawingsound.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a7f.drawingsound.R;
import com.a7f.drawingsound.model.Sheet;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SheetAdapter extends RecyclerView.Adapter<SheetAdapter.ViewHolder> {

    private ArrayList<Sheet> items = new ArrayList<>();

    @NonNull
    @Override
    public SheetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sheet, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SheetAdapter.ViewHolder viewHolder, int position) {

        Sheet item = items.get(position);

        Glide.with(viewHolder.itemView.getContext())
                .load(item.getUrl())
                .into(viewHolder.ImageViewSheet);

        viewHolder.TextViewTitle.setText(item.getTitle());
        viewHolder.TextViewComposer.setText(item.getComposer());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Sheet> items) {
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ImageViewSheet;

        TextView TextViewTitle, TextViewComposer;

        ViewHolder(View itemView) {
            super(itemView);

            ImageViewSheet = itemView.findViewById(R.id.ImageViewSheet);

            TextViewTitle = itemView.findViewById(R.id.TextViewTitle);
            TextViewComposer = itemView.findViewById(R.id.TextViewComposer);

        }
    }
}
