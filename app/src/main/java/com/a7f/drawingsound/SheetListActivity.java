package com.a7f.drawingsound;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.a7f.drawingsound.adapter.SheetAdapter;
import com.a7f.drawingsound.data.SampleData;

public class SheetListActivity extends AppCompatActivity {

    private SheetAdapter adapter = new SheetAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_list);

        //recycleView 초기화
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

       // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        //new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
       recyclerView.setAdapter(adapter);



        //recyclerView.setAdapter(adapter);
        adapter.setItems(new SampleData().getItems());

    }
}
