package com.witelokk.prac5;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.witelokk.prac5.databinding.ActivityCategoriesBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {
    ActivityCategoriesBinding binding;
    ArrayList<Pair<String, List<String>>> fruitVarieties = new ArrayList<>(Arrays.asList(
            new Pair<>("Apples", Arrays.asList("Granny Smith", "Red Delicious", "Golden Delicious")),
            new Pair<>("Bananas", Arrays.asList("Cavendish", "Lady Finger", "Red Banana")),
            new Pair<>("Oranges", Arrays.asList("Navel", "Valencia", "Blood Orange")),
            new Pair<>("Grapes", Arrays.asList("Thompson Seedless", "Concord", "Muscat")),
            new Pair<>("Strawberries", Arrays.asList("Albion", "Seascape", "Chandler"))
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoriesBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(binding.toolbar);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getCategories());
        binding.categoriesListView.setAdapter(adapter);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    Intent data = result.getData();
                    fruitVarieties.set(getCategories().indexOf(data.getStringExtra("name")),
                            new Pair<>(data.getStringExtra("name"), data.getStringArrayListExtra("varieties")));
                });

        binding.categoriesListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, CategoryActivity.class);
            intent.putExtra("name", fruitVarieties.get(position).first);
            intent.putStringArrayListExtra("varieties", new ArrayList<>(fruitVarieties.get(position).second));
            activityResultLauncher.launch(intent);
        });
    }

    private List<String> getCategories() {
        ArrayList<String> categories = new ArrayList<>();
        for (Pair<String, List<String>> p: fruitVarieties) {
            categories.add(p.first);
        }
        return categories;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_scrollview) {
            startActivity(new Intent(this, ScrollViewActivity.class));
        } else if (item.getItemId() == R.id.menu_recyclerview) {
            startActivity(new Intent(this, RecyclerViewActivity.class));
        } else if (item.getItemId() == R.id.menu_spinner) {
            startActivity(new Intent(this, SpinnerActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}