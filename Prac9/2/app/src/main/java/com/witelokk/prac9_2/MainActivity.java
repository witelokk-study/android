package com.witelokk.prac9_2;

import android.os.Bundle;
import android.os.Environment;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

        findViewById(R.id.button_save).setOnClickListener(v -> {
            String filename = ((EditText) findViewById(R.id.edit_text_filename)).getText().toString();
            String text = ((EditText) findViewById(R.id.edit_text_text)).getText().toString();

            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }
            File file = new File(storageDir, filename);
            try {
                if (!file.exists()) {
                    boolean created = file.createNewFile();
                    if (created) {
                        FileWriter writer = new FileWriter(file);
                        writer.append(text);
                        writer.flush();
                        writer.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}