package com.witelokk.prac7;

import android.app.Service;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer = null;
    private Uri trackUri;
    private final Binder binder = new Binder(this);

    static class Binder extends android.os.Binder {
        private final MusicService service;

        Binder(MusicService service) {
            this.service = service;
        }

        public boolean hasTrack() {
            return service.mediaPlayer != null;
        }

        public void setTrack(Uri uri) {
            if (service.mediaPlayer != null) {
                service.mediaPlayer.stop();
            }
            service.trackUri = uri;
            service.mediaPlayer = MediaPlayer.create(service, uri);
        }

        public void startTrack() {
            if (service.mediaPlayer == null)
                return;
            service.mediaPlayer.start();
        }

        public void pauseTrack() {
            if (service.mediaPlayer == null)
                return;
            service.mediaPlayer.pause();
        }

        public void seekTrackTo(int msec) {
            if (service.mediaPlayer == null)
                return;
            service.mediaPlayer.seekTo(msec);
        }

        public boolean isPlaying() {
            if (service.mediaPlayer == null)
                return false;
            return service.mediaPlayer.isPlaying();
        }

        public int getTrackDuration() {
            if (service.mediaPlayer == null)
                return 0;
            return service.mediaPlayer.getDuration();
        }

        public int getCurrentPosition() {
            if (service.mediaPlayer == null)
                return 0;
            return service.mediaPlayer.getCurrentPosition();
        }

        public @Nullable MediaMetadataRetriever getMetadataRetriever() {
            if (service.trackUri == null)
                return null;
            MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
            metadataRetriever.setDataSource(service, service.trackUri);
            return metadataRetriever;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }
}