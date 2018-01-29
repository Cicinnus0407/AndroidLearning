package com.cicinnus.androidlearning.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.cicinnus.androidlearning.R;
import com.cicinnus.androidlearning.messenger.MessengerTest;

/**
 * @author cicinnus
 *         2018/1/23.
 */

public class DragViewActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_view);
        Button btn = findViewById(R.id.btn_start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DragViewActivity.this, MessengerTest.class);
                startActivity(intent);
            }
        });

    }
}
