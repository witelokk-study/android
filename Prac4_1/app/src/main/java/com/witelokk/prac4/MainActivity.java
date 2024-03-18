package com.witelokk.prac4;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.color.DynamicColors;
import com.witelokk.prac4.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity {
    MainActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        binding.nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SecondActivity.class);
            intent.putExtra("name", binding.nameEditText.getText().toString());
            startActivity(intent);
        });
    }
}