package com.a7f.drawingsound;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.a7f.drawingsound.adapter.MoodAdapter;
import com.a7f.drawingsound.data.MoodsData;
import com.a7f.drawingsound.model.Mood;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import static com.a7f.drawingsound.model.Mood.mood;

public class MoodListActivity extends AppCompatActivity {

    private MoodAdapter adapter = new MoodAdapter();

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_list);

        //recycleView 초기화
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        //아이템 로드
        adapter.setItems(new MoodsData().getItems());

        //recyclerView.setOnClickListener(new ImageClickListener());

        adapter.setOnItemClickListener(new MoodAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(MoodAdapter.ViewHolder holder, View view, int position) {
                String mood = adapter.getKeyMood(position);
                Intent intent = new Intent(MoodListActivity.this,SheetListActivity.class);
                intent.putExtra("Mood",mood);
                startActivity(intent);
               // Toast.makeText(getApplicationContext(), mood,Toast.LENGTH_LONG).show();
            }
        });
    }

//    class ImageClickListener implements View.OnClickListener{
//
//
//        @Override
//        public void onClick(View v) {
//            Intent intent = new Intent(MoodListActivity.this,SheetListActivity.class);
//            startActivity(intent);
//        }
//    }


}