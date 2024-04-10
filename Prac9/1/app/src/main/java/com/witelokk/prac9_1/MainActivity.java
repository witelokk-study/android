package com.witelokk.prac9_1;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.witelokk.prac9_1.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private static String FILENAME = "filename";
    private static String TEXT = "text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.editTextFilename.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                binding.buttonSave.setEnabled(s.length() != 0);
                binding.buttonRead.setEnabled(s.length() != 0);
                binding.buttonDelete.setEnabled(s.length() != 0);
                binding.buttonAppend.setEnabled(s.length() != 0);
            }
        });

        binding.buttonSave.setOnClickListener(v -> {
            try (FileOutputStream fos = this.openFileOutput(getFilename(), Context.MODE_PRIVATE)) {
                fos.write(getText().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        binding.buttonRead.setOnClickListener(v -> {
            binding.textView.setText("");
            try (FileInputStream fis = this.openFileInput(getFilename())) {
                InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
                StringBuilder stringBuilder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                    String line = reader.readLine();
                    while (line != null) {
                        stringBuilder.append(line).append('\n');
                        line = reader.readLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                binding.textView.setText(stringBuilder.toString());
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "File does not exit", Toast.LENGTH_SHORT).show();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        binding.buttonAppend.setOnClickListener(v -> {
            try (FileOutputStream fos = this.openFileOutput(getFilename(), Context.MODE_PRIVATE | Context.MODE_APPEND)) {
                fos.write((" " + getText()).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        binding.buttonDelete.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Deleting file")
                    .setMessage(String.format("Do you really want to delete %s?", getFilename()))
                    .setPositiveButton("Delete", ((dialog, which) -> {
                        binding.textView.setText("");
                        File dir = getFilesDir();
                        File file = new File(dir, getFilename());
                        if (!file.delete()) {
                            Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "File deleted", Toast.LENGTH_SHORT).show();
                        }
                    }))
                    .setNegativeButton("Cancel", ((dialog, which) -> dialog.dismiss()))
                    .create()
                    .show();
        });
    }

    private String getFilename() {
        return binding.editTextFilename.getText().toString();
    }


    private String getText() {
        return binding.editTextText.getText().toString();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FILENAME, binding.editTextFilename.getText().toString());
        outState.putString(TEXT, binding.editTextText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        binding.editTextFilename.setText(savedInstanceState.getString(FILENAME));
        binding.editTextText.setText(savedInstanceState.getString(TEXT));
    }
}