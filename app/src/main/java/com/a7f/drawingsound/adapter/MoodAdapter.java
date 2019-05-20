package com.a7f.drawingsound.adapter;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.a7f.drawingsound.HummingFFTActivity;
import com.a7f.drawingsound.MoodListActivity;
import com.a7f.drawingsound.R;
import com.a7f.drawingsound.SetActivity;
import com.a7f.drawingsound.model.Mood;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.ViewHolder> {

    private ArrayList<Mood> items = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public MoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mood, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        OnItemClickListener listener;

        return viewHolder;
    }

    public static interface OnItemClickListener{
        public void onItemClick(ViewHolder holder, View view, int position);
    }

    @Override
    public void onBindViewHolder(@NonNull MoodAdapter.ViewHolder viewHolder, int position) {

        Mood item = items.get(position);

        Glide.with(viewHolder.itemView.getContext())
                .load(item.getImg())
                .into(viewHolder.ivMovie);

        viewHolder.setOnItemClickListener(listener);
       // viewHolder.itemView.setOnClickListener((View.OnClickListener) this);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    //public void onClick
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Mood> items) {
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivMovie;
        TextView tvTitle, tvContent, tvGenre;

        OnItemClickListener listener;

        ViewHolder(final View itemView) {
            super(itemView);

            ivMovie = itemView.findViewById(R.id.iv_item_movie);

            tvTitle = itemView.findViewById(R.id.tv_item_movie_title);
//            tvContent = itemView.findViewById(R.id.tv_item_movie_content);
//            tvGenre = itemView.findViewById(R.id.tv_item_movie_genre);
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener !=null ){
                        listener.onItemClick(ViewHolder.this, itemView, position);
                    }
                }
            });

        }


        void onBind(Mood mood){


        }

        @Override
        public void onClick(View v) {

        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

//        @Override
//        public void onClick(View v) {
////            switch (v.getId()){
////                case R.tvTitle!="dj"
////            }
//            Intent intent = new Intent(MoodListActivity.this, HummingFFTActivity.class);
//            startActivity(intent);
//
//        }

    }

}