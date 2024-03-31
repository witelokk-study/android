package com.witelokk.prac7;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.witelokk.prac7.databinding.FragmentMusicBinding;

public class MusicFragment extends Fragment {
    FragmentMusicBinding binding;
    ServiceConnection serviceConnection;
    MusicService.Binder service;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMusicBinding.inflate(inflater, container, false);

        ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri == null)
                return;
            service.setTrack(uri);
            loadTrack();
        });

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicFragment.this.service = (MusicService.Binder) service;
                if (MusicFragment.this.service.hasTrack()) {
                    loadTrack();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        Intent intent = new Intent(getActivity(), MusicService.class);

        getActivity().bindService(intent, serviceConnection, 0);

        binding.buttonSelectTrack.setOnClickListener(v -> {
            activityResultLauncher.launch("audio/*");
        });

        binding.playPauseButton.setOnClickListener(v -> {
            if (service.isPlaying()) {
                service.pauseTrack();
            } else {
                service.startTrack();
            }
        });

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean trackingTouch = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (trackingTouch)
                    service.seekTrackTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                trackingTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                trackingTouch = false;
            }
        });

        Handler handler = new Handler();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (service != null) {
                    if (service.isPlaying()) {
                        binding.seekBar.setProgress(service.getCurrentPosition());
                        binding.playPauseButton.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_pause_24, null));
                    } else {
                        binding.playPauseButton.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_play_arrow_24, null));
                    }
                }
                handler.postDelayed(this, 50);
            }
        });

        return binding.getRoot();
    }

    void loadTrack() {
        String trackAuthor = service.getMetadataRetriever().extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String trackTitle = service.getMetadataRetriever().extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        binding.trackName.setText(String.format("%s — %s", trackAuthor, trackTitle));

        // make marquee animation work
        binding.trackName.setSelected(true);

        byte[] image = service.getMetadataRetriever().getEmbeddedPicture();
        if (image != null) {
            binding.imageView.setVisibility(View.VISIBLE);
            binding.imageView.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
        } else {
            binding.imageView.setVisibility(View.GONE);
        }

        binding.seekBar.setMax(service.getTrackDuration());
        binding.seekBar.setProgress(service.getCurrentPosition());
        binding.playPauseButton.setEnabled(true);
    }
}
