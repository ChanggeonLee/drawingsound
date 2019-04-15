package com.a7f.drawingsound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SheetActivity extends AppCompatActivity {

    Button ButtonApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);

        ButtonApply = (Button)findViewById(R.id.ButtonApply);
        ButtonApply.setOnClickListener(Applyonclick);

    }

    Button.OnClickListener Applyonclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SheetActivity.this, SheetSettingActivity.class);
            startActivity(intent);
        }
    };

}
