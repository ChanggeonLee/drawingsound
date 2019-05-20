package com.a7f.drawingsound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.a7f.drawingsound.adapter.MoodAdapter;
import com.a7f.drawingsound.adapter.SheetAdapter;
import com.a7f.drawingsound.data.SheetsData;
import com.a7f.drawingsound.model.Sheet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class SheetListActivity extends AppCompatActivity {

    private SheetAdapter adapter = new SheetAdapter();

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_list);

        // Firebase setting
        onSetFirebase();


    }

    private void onSetRecyclerView(SheetsData sheetsData){
        //recycleView 초기화
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        //new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        recyclerView.setAdapter(adapter);

        adapter.setItems(sheetsData.getItems());
        //recyclerView.setAdapter(adapter);
        //adapter.setItems(new SampleData().getItems());
        adapter.setOnItemClickListener(new SheetAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(SheetAdapter.ViewHolder holder, View view, int position) {
//                long data = adapter.getItemId(position);
                String key = adapter.getkey(position);
                Log.e("adapter key",key);
                Intent intent = new Intent(SheetListActivity.this, ViewScore.class);
                intent.putExtra("sheetKey",key);
                startActivity(intent);
            }
        });

    }


    ValueEventListener sheetListener = new ValueEventListener() {
        Sheet sheet;
        SheetsData sheetsData;
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            sheetsData = new SheetsData();

            Intent intent = getIntent();
            String mood = intent.getStringExtra("Mood");
//            Log.e("get mood",mood);

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                sheet = snapshot.getValue(Sheet.class);
//                Log.e("mood",sheet.getMood());
                if(mood.equals(sheet.getMood())){
                    sheet.setKey(snapshot.getKey());
//                    Log.e("Database",sheet.getMood());
                    sheetsData.setItems(sheet);
                }
            }
            onSetRecyclerView(sheetsData);
            Log.d("SheetListActivity", "Single ValueEventListener : " + sheetsData);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void onSetFirebase(){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("sheets").child(currentUser.getUid());
        myRef.addListenerForSingleValueEvent(sheetListener);
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(SheetListActivity.this, SetActivity.class);
        finish();
//        startActivity(intent);
    }
}
