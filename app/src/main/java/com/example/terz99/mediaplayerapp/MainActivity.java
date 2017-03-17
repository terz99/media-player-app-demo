package com.example.terz99.mediaplayerapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the resource for the audio player
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.audio_file);

        // get the resource for the start button
        // and set click listener
        Button startButton = (Button) findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // start the audio
                mediaPlayer.start();
            }
        });

        // get the resource for the stop button
        // and set click listener
        Button stopButton = (Button) findViewById(R.id.button_stop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /**
                 * The stop button reset the audio file
                 * so that next time it will play the audio from the beginning
                 */
                mediaPlayer.seekTo(0);
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
            }
        });

        // get the resource for the pause button
        // and set click listener
        Button pauseButton = (Button) findViewById(R.id.button_pause);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // if alredy playing -> pause the audio
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
            }
        });

        // get the resource for the set to mid button
        // and set click listener
        Button setToMidButton = (Button) findViewById(R.id.button_set_to_mid);
        setToMidButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /**
                 * Get the duration of the whole song and divide it to 2
                 * That way we will get the middle of the song in miliseconds
                 */
                int audioDuration = mediaPlayer.getDuration();
                mediaPlayer.seekTo(audioDuration/2);
            }
        });
    }
}
