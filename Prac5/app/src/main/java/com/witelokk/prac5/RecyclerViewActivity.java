package com.witelokk.prac5;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.witelokk.prac5.databinding.ActivityRecyclerViewBinding;
import com.witelokk.prac5.databinding.ItemRecyclerviewBinding;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {
    ActivityRecyclerViewBinding binding;

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private final LayoutInflater inflater;
        private final List<String> items;
        private final Context context;

        class ViewHolder extends RecyclerView.ViewHolder {
            final ItemRecyclerviewBinding binding;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                binding = ItemRecyclerviewBinding.bind(itemView);
            }

            public void setOnClickListener(View.OnClickListener l) {
                binding.getRoot().setOnClickListener(l);
            }
        }

        RecyclerViewAdapter(Context context, List<String> items) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_recyclerview, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String item = items.get(position);
            holder.setOnClickListener(v -> {
                Toast.makeText(context, item, Toast.LENGTH_SHORT).show();
                Log.d("recycler_view_adapter", "item " + item);
            });
            holder.binding.textView.setText(item);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecyclerViewBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            strings.add(Integer.valueOf(i).toString());
        }
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(new RecyclerViewAdapter(this, strings));
    }
}
