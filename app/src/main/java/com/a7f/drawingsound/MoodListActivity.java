package com.a7f.drawingsound;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.a7f.drawingsound.adapter.MoodAdapter;
import com.a7f.drawingsound.data.MoodsData;
import com.a7f.drawingsound.model.Mood;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import static com.a7f.drawingsound.model.Mood.mood;

public class MoodListActivity extends AppCompatActivity {

    private MoodAdapter adapter = new MoodAdapter();

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_list);
        setTitle("");

        //recycleView 초기화
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        //아이템 로드
        adapter.setItems(new MoodsData().getItems());
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

        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(tb);

        mAuth = FirebaseAuth.getInstance();
    }

    private void signOut() {

        new AlertDialog.Builder(this).setTitle("LOGOUT").setMessage("로그아웃 하시겠습니까?").setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                Intent i = new Intent(MoodListActivity.this,SigninActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_action, menu) ;

        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout :
                signOut();
                return true ;
            case R.id.action_license :
                Intent intent = new Intent(MoodListActivity.this,LicenseActivity.class);
                startActivity(intent);
               // finish();
                return true;
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }
}

