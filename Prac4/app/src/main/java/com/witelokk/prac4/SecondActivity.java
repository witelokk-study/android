package com.witelokk.prac4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.witelokk.prac4.databinding.SecondActivityBinding;

public class SecondActivity extends AppCompatActivity {
    SecondActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SecondActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textView.setText(getIntent().getStringExtra("name"));

        binding.nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ThirdActivity.class);

            Bundle bundle = new Bundle();
            bundle.putString("name", getIntent().getStringExtra("name"));
            bundle.putString("subject", binding.subjectTextEdit.getText().toString());

            startActivity(intent);
        });

        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Intent data = result.getData();
            Toast.makeText(this, getText(R.string.day) + ": " + data.getStringExtra("day"), Toast.LENGTH_LONG).show();
        });
    }
}
