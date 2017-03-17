package com.example.terz99.mediaplayerapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // Global media player object
    private MediaPlayer mediaPlayer;

    // the maximum volume the audio file can have
    final static int maxVolume = 100;

    // the current volume the audio file has
    private int currentVolume;


    /**
     * This method creates the MainActivity
     * @param savedInstanceState
     */
    protected void onCreate(final Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // get the resource for the audio player
        mediaPlayer = MediaPlayer.create(this, R.raw.audio_file);

        // set the currentVolume to 50
        currentVolume = 50;
        setVolume(currentVolume);

        // find volume TextView
        final TextView textViewVolume = (TextView) findViewById(R.id.textview_volume);
        // set current volume to the text view
        textViewVolume.setText(Integer.toString(currentVolume));

        // get the resource for the start button
        // and set click listener
        Button startButton = (Button) findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // start the audio
                if(mediaPlayer.isPlaying() == false){
                    mediaPlayer.start();
                }
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
                if(mediaPlayer.isPlaying() || mediaPlayer.getCurrentPosition() != 0) {
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
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
        final Button setToMidButton = (Button) findViewById(R.id.button_set_to_mid);
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


        /**
         * Set the progress on the volume seekbar to currentVolume
         * Override the methods of the seekbar
         */
        SeekBar volumeSeekBar = (SeekBar) findViewById(R.id.seekbar_volume);
        volumeSeekBar.setProgress(currentVolume);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {



            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                /**
                 * Get the current progress (@param progress) and set it to
                 * currentVolume, set the volume and display it to screen
                 */
                currentVolume = progress;
                setVolume(currentVolume);
                textViewVolume.setText(Integer.toString(currentVolume));
            }



            public void onStartTrackingTouch(SeekBar seekBar) {

                /**
                 * Get the current progress for the seekbar and set it to
                 * currentVolume, set the volume and display it to screen
                 */
                currentVolume = seekBar.getProgress();
                setVolume(currentVolume);
                textViewVolume.setText(Integer.toString(currentVolume));
            }



            public void onStopTrackingTouch(SeekBar seekBar) {

                /**
                 * Get the current progress for the seekbar and set it to
                 * currentVolume, set the volume and display it to screen
                 */
                currentVolume = seekBar.getProgress();
                setVolume(currentVolume);
                textViewVolume.setText(Integer.toString(currentVolume));
            }
        });


        /**
         * This button moves the time of the song backwards for 5000ms
         */
        Button backwardsButton = (Button) findViewById(R.id.backwards_button);
        backwardsButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                int newPosition = mediaPlayer.getCurrentPosition() - 5000;
                if(newPosition < 0){
                    newPosition = 0;
                }

                mediaPlayer.seekTo(newPosition);
            }
        });



        /**
         * This button moves the time of the song forwards for 5000ms
         */
        Button forwardsButton = (Button) findViewById(R.id.forwards_button);
        forwardsButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                int newPosition = mediaPlayer.getCurrentPosition() + 5000;
                if(newPosition > mediaPlayer.getDuration()){
                    newPosition = mediaPlayer.getDuration();
                }

                mediaPlayer.seekTo(newPosition);
            }
        });
    }



    /**
     * This method sets the volume to currentVolume
     * @param currentVolume the current volume of the audio file
     */
    private void setVolume(int currentVolume) {
        // logarithmic value for the volume
        float volume = 1 - (float) (Math.log(maxVolume-currentVolume)/Math.log(maxVolume));
        mediaPlayer.setVolume(volume, volume);
    }


    /**
     * This method puts the MainActivity in background and runs it
     * until  killed
     */
    protected void onPause() {

        super.onPause();

        if(mediaPlayer.isPlaying() || mediaPlayer.getCurrentPosition() != 0) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }
    }
}
