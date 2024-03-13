package com.witelokk.prac3;

import android.graphics.Point;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.button).setOnClickListener(v -> { 
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) v.getLayoutParams();
            layoutParams.height = 200;
            v.setLayoutParams(layoutParams);
        });

        findViewById(R.id.button2).setOnClickListener(v -> {
            v.setPadding(10, 10, 10, 10);
        });
        
        findViewById(R.id.button4).setOnClickListener(v -> {
            Point size = new Point();
            getWindowManager().getDefaultDisplay().getSize(size);
            Toast.makeText(this, String.format("Screen size: %dx%d", size.x, size.y), Toast.LENGTH_SHORT).show();
        });
    }
}