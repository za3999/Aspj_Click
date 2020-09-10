package com.test.temp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.test.aspj.ClickIgnore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.test2Click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplication(), "test2Click", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.test3Click).setOnClickListener(view -> Toast.makeText(getApplication(), "test3Click", Toast.LENGTH_SHORT).show());
    }

    @ClickIgnore
    public void test1Click(View view) {
        Toast.makeText(getApplication(), "test1Click", Toast.LENGTH_SHORT).show();
    }
}