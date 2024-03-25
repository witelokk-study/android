package com.witelokk.prac5;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.witelokk.prac5.databinding.ActivityCategoryBinding;
import com.witelokk.prac5.databinding.DialogAddVarietyBinding;
import com.witelokk.prac5.databinding.DialogRemoveVarietyBinding;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    ActivityCategoryBinding binding;
    String categoryName;
    ArrayList<String> varieties;
    ArrayAdapter varietiesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        categoryName = getIntent().getStringExtra("name");
        varieties = getIntent().getStringArrayListExtra("varieties");

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        varietiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, varieties);
        binding.categoriesListView.setAdapter(varietiesAdapter);

        binding.categoriesListView.setOnItemClickListener((parent, view, position, id) -> {
            showRemoveDialog(position);
        });

        binding.fabAdd.setOnClickListener(v -> showAddDialog());
    }

    void showAddDialog() {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
        DialogAddVarietyBinding dialogBinding = DialogAddVarietyBinding.inflate(getLayoutInflater());
        dialogBuilder.setView(dialogBinding.getRoot());

        dialogBuilder.setPositiveButton(R.string.add, (dialog, whichButton) -> {
            String text = dialogBinding.editTextVariety.getText().toString();
            varieties.add(text);
            varietiesAdapter.notifyDataSetChanged();
        });
        dialogBuilder.setNegativeButton(R.string.cancel, (dialog, whichButton) -> {});

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    void showRemoveDialog(int position) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
        DialogRemoveVarietyBinding dialogBinding = DialogRemoveVarietyBinding.inflate(getLayoutInflater());
        dialogBinding.textView.setText(getString(R.string.delete_variety_description, varieties.get(position)));
        dialogBuilder.setView(dialogBinding.getRoot());

        dialogBuilder.setTitle(R.string.delete_variety);
        dialogBuilder.setPositiveButton(R.string.delete, (dialog, whichButton) -> {
            varieties.remove(position);
            varietiesAdapter.notifyDataSetChanged();
        });
        dialogBuilder.setNegativeButton(R.string.cancel, (dialog, whichButton) -> {});

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("name", categoryName);
        intent.putExtra("varieties", varieties);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
