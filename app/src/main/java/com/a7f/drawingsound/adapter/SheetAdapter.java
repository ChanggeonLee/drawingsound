package com.a7f.drawingsound.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a7f.drawingsound.R;
import com.a7f.drawingsound.model.Sheet;
import java.util.ArrayList;


public class SheetAdapter extends RecyclerView.Adapter<SheetAdapter.ViewHolder> {

    private ArrayList<Sheet> items = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public SheetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View item2View = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sheet, parent, false);
        ViewHolder viewHolder = new ViewHolder(item2View);
        OnItemClickListener listener;
        return viewHolder;
    }

    public static interface OnItemClickListener{
        public void onItemClick(ViewHolder holder, View view, int position);
    }

    @Override
    public void onBindViewHolder(@NonNull SheetAdapter.ViewHolder viewHolder, int position) {

        Sheet item = items.get(position);

        viewHolder.TextViewTitle.setText(item.getTitle());
        viewHolder.TextViewComposer.setText(item.getComposer());
        viewHolder.TextViewDate.setText(item.getDate());
        viewHolder.TextViewMood.setText(item.getMood());

        viewHolder.setOnItemClickListener(listener);

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Sheet> items) {
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView TextViewTitle, TextViewComposer, TextViewDate, TextViewMood;
        OnItemClickListener listener;

        ViewHolder(final View item2View) {
            super(item2View);

            TextViewTitle = item2View.findViewById(R.id.TextViewTitle);
            TextViewComposer = item2View.findViewById(R.id.TextViewComposer);
            TextViewDate = item2View.findViewById(R.id.TextViewDate);
            TextViewMood = item2View.findViewById(R.id.TextViewMood);
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener !=null ){
                        listener.onItemClick(ViewHolder.this, item2View, position);
                    }
                }
            });

        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }
    }

    public String getkey(int position){
        Sheet item = items.get(position);
        return item.getKey();
    }
}

