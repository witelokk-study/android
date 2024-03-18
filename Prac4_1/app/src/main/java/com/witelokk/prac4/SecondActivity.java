package com.witelokk.prac4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.witelokk.prac4.databinding.SecondActivityBinding;

public class SecondActivity extends AppCompatActivity {
    SecondActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SecondActivityBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        binding.firstLastName.setText(getString(R.string.name,
                getIntent().getStringExtra("name")));

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    Intent data = result.getData();

                    binding.info.setVisibility(View.VISIBLE);
                    binding.info.setText(getString(R.string.info,
                    data.getStringExtra("day"),
                    data.getStringExtra("time"),
                    data.getStringExtra("comment")));
        });

        binding.nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ThirdActivity.class);

            Bundle bundle = new Bundle();
            bundle.putString("name", getIntent().getStringExtra("name"));
            bundle.putString("subject", binding.subjectTextEdit.getText().toString());

            activityResultLauncher.launch(intent);
        });
    }
}
