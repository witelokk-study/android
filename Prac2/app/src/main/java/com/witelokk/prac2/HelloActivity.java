package com.witelokk.prac2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HelloActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hello);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        TextView helloTextView = findViewById(R.id.hello_text_view);
        helloTextView.setText(getString(R.string.hello, name));
    }
}
