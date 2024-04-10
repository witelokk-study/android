package com.witelokk.prac8;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.witelokk.prac8.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    static final private String URL = "https://random.dog/woof.json?include=jpg,png";
    static final private String PATH_KEY = "path";
    static final private String DELAY_KEY = "delay";
    static final private String MESSAGE_KEY = "message";
    ActivityMainBinding binding;

    public static class LoadImageWork extends Worker {
        public LoadImageWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
        }

        @NonNull
        @Override
        public Result doWork() {
            String imageUrl = getImageUrl();

            String filename = null;

            {
                String[] parts = imageUrl.split("/");
                filename = parts[parts.length-1];
            }

            Log.d("LoadImageWork Image Url", imageUrl);

            URL url;
            try {
                url = new URL(imageUrl);
            } catch (MalformedURLException e) {
                return Result.failure();
            }

            InputStream inputStream;
            try {
                inputStream = url.openStream();
            } catch (IOException e) {
                return Result.failure();
            }

            FileOutputStream outputStream;
            try {
                outputStream = getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
            } catch (IOException e) {
                return Result.failure();
            }

            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while (true) {
                try {
                    if ((bytesRead = inputStream.read(dataBuffer, 0, 1024)) == -1) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    outputStream.write(dataBuffer, 0, bytesRead);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            Data outputData = new Data.Builder()
                    .putString(PATH_KEY, filename)
                    .build();

            return Result.success(outputData);
        }

        private String getJson() {
            URL url = null;
            try {
                url = new URL(URL);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            BufferedReader in = null;
            try {
                in = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String inputLine;
            StringBuilder result = new StringBuilder();

            while (true) {
                try {
                    if ((inputLine = in.readLine()) == null) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                result.append(inputLine);
            }

            return result.toString();
        }

        private String getImageUrl() {
            String json = getJson();
            Log.d("LoadImageWork JSON", json);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(json);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            try {
                return jsonObject.getString("url");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class ToastWorker extends Worker {
        public ToastWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
        }

        @NonNull
        @Override
        public Result doWork() {
            Data inputData = getInputData();

            int delay = inputData.getInt(DELAY_KEY, 0);
            String message = inputData.getString(MESSAGE_KEY);

            try {
                Log.d("ToastWorker", "test 1");
                Thread.sleep(delay);
                Log.d("ToastWorker", "test 2");
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return Result.success();
        }
    }

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

        WorkManager workManager = WorkManager.getInstance(getApplicationContext());

        binding.buttonStart3TasksSequentially.setOnClickListener(v -> {
            Data data1 = new Data.Builder()
                    .putString(MESSAGE_KEY, "1")
                    .putInt(DELAY_KEY, 3000)
                    .build();
            Data data2 = new Data.Builder()
                    .putString(MESSAGE_KEY, "2")
                    .putInt(DELAY_KEY, 8000)
                    .build();
            Data data3 = new Data.Builder()
                    .putString(MESSAGE_KEY, "3")
                    .putInt(DELAY_KEY, 1000)
                    .build();

            OneTimeWorkRequest workRequest1 = new OneTimeWorkRequest.Builder(ToastWorker.class)
                    .setInputData(data1)
                    .build();
            OneTimeWorkRequest workRequest2 = new OneTimeWorkRequest.Builder(ToastWorker.class)
                    .setInputData(data2)
                    .build();
            OneTimeWorkRequest workRequest3 = new OneTimeWorkRequest.Builder(ToastWorker.class)
                    .setInputData(data3)
                    .build();

            workManager
                    .beginWith(workRequest1)
                    .then(workRequest2)
                    .then(workRequest3)
                    .enqueue();
        });

        binding.buttonStart2TasksInParallel.setOnClickListener(v -> {
            Data data1 = new Data.Builder()
                    .putString(MESSAGE_KEY, "1")
                    .putInt(DELAY_KEY, 5000)
                    .build();
            Data data2 = new Data.Builder()
                    .putString(MESSAGE_KEY, "2")
                    .putInt(DELAY_KEY, 100)
                    .build();

            OneTimeWorkRequest workRequest1 = new OneTimeWorkRequest.Builder(ToastWorker.class)
                    .setInputData(data1)
                    .build();
            OneTimeWorkRequest workRequest2 = new OneTimeWorkRequest.Builder(ToastWorker.class)
                    .setInputData(data2)
                    .build();

            workManager.enqueue(Arrays.asList(workRequest1, workRequest2));
        });

        binding.buttonLoadImage.setOnClickListener(v -> {
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(LoadImageWork.class).build();
            workManager.enqueue(workRequest);

            workManager.getWorkInfoByIdLiveData(workRequest.getId()).observe(this, workInfo -> {
                if (workInfo != null && workInfo.getState().isFinished()) {
                    Log.d("LoadImageWork", "finished");
                    String path = workInfo.getOutputData().getString(PATH_KEY);
                    try {
                        FileInputStream inputStream = getApplicationContext().openFileInput(path);
                        Bitmap bm = BitmapFactory.decodeStream(inputStream);
                        binding.imageView.setImageBitmap(bm);
                    } catch (FileNotFoundException ignored) {}
                }
            });
        });


    }
}