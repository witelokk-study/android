package com.witelokk.prac4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.witelokk.prac4.databinding.ThirdActivityBinding;

public class ThirdActivity extends AppCompatActivity {
    ThirdActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ThirdActivityBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        binding.buttonOk.setOnClickListener(v -> {
            Intent intent = new Intent();

            intent.putExtra("day", binding.dayEditText.getText().toString());
            intent.putExtra("time", binding.timeEditText.getText().toString());
            intent.putExtra("comment", binding.commentEditText.getText().toString());

            setResult(AppCompatActivity.RESULT_OK, intent);
            finish();
        });
    }
}
