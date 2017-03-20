package com.example.terz99.mediaplayerapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
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

    // AudioManager instance for managing the audio focus
    private AudioManager audioManager;

    // Audio Focus change listener
    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {

        @Override
        public void onAudioFocusChange(int focusChange) {

            if(focusChange == AudioManager.AUDIOFOCUS_GAIN){

                if(mediaPlayer != null){
                    mediaPlayer.start();
                }
            } else if(focusChange == AudioManager.AUDIOFOCUS_LOSS){

                releaseMediaPlayer();
            } else if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){

                if(mediaPlayer != null){
                    mediaPlayer.pause();
                }
            }
        }
    };

    // MediaPlayer completion listener - what happens when the audio file finishes playing
    MediaPlayer.OnCompletionListener mediaPlayerCompletionListener = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            // release memory used by the mediaPlayer
            releaseMediaPlayer();
        }
    };


    /**
     * This method creates the MainActivity
     * @param savedInstanceState
     */
    protected void onCreate(final Bundle savedInstanceState) {

        // create this activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get system srevice for the AudioManager instance
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // set the currentVolume to 50
        currentVolume = 50;

        // find volume TextView
        final TextView textViewVolume = (TextView) findViewById(R.id.textview_volume);
        // set current volume to the text view
        textViewVolume.setText(Integer.toString(currentVolume));

        // get the resource for the start button
        // and set click listener
        Button startButton = (Button) findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // get audio focus request
                int requestResult = audioManager.requestAudioFocus(audioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

                // check if audio focus request was GRANTED
                if(requestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){

                    if(mediaPlayer == null){
                        // get the resource file for the audio if not already got
                        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.audio_file);
                    }
                    // set the volume before playing
                    setVolume(currentVolume);
                    // play the audio file
                    mediaPlayer.start();
                    // set completion listener to the mediaPlayer
                    mediaPlayer.setOnCompletionListener(mediaPlayerCompletionListener);
                }
            }
        });




        // get the resource for the stop button
        // and set click listener
        Button stopButton = (Button) findViewById(R.id.button_stop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // if mediaPlayer is not initialized then do nothing (return)
                if(mediaPlayer == null) return;

                /**
                 * The stop button reset the audio file
                 * so that next time it will play the audio from the beginning
                 */
                if(mediaPlayer.isPlaying()){
                    // if the mediaPlayer is playing then pause it
                    mediaPlayer.pause();
                }
                // release the memory used by the audio file
                releaseMediaPlayer();
            }
        });




        // get the resource for the pause button
        // and set click listener
        Button pauseButton = (Button) findViewById(R.id.button_pause);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // if mediaPlayer is not initialized then do nothing (return)
                if(mediaPlayer == null) return;

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

                // if mediaPlayer is not initialized then do nothing (return)
                if(mediaPlayer == null) return;

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
                textViewVolume.setText(Integer.toString(currentVolume));

                // if mediaPlayer is not initialized then do nothing (return)
                if(mediaPlayer == null) return;

                setVolume(currentVolume);
            }



            public void onStartTrackingTouch(SeekBar seekBar) {

                /**
                 * Get the current progress for the seekbar and set it to
                 * currentVolume, set the volume and display it to screen
                 */
                currentVolume = seekBar.getProgress();
                textViewVolume.setText(Integer.toString(currentVolume));

                // if mediaPlayer is not initialized then do nothing (return)
                if(mediaPlayer == null) return;

                setVolume(currentVolume);
            }



            public void onStopTrackingTouch(SeekBar seekBar) {

                /**
                 * Get the current progress for the seekbar and set it to
                 * currentVolume, set the volume and display it to screen
                 */
                currentVolume = seekBar.getProgress();
                textViewVolume.setText(Integer.toString(currentVolume));

                // if mediaPlayer is not initialized then do nothing (return)
                if(mediaPlayer == null) return;

                setVolume(currentVolume);
            }
        });


        /**
         * This button moves the time of the song backwards for 5000ms
         */
        Button backwardsButton = (Button) findViewById(R.id.backwards_button);
        backwardsButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // if mediaPlayer is not initialized then do nothing (return)
                if(mediaPlayer == null) return;

                // get the new position for the mediaPlayer
                int newPosition = mediaPlayer.getCurrentPosition() - 5000;
                if(newPosition < 0){
                    // if the position is negative we need to put it to 0
                    newPosition = 0;
                }
                // set the mediaPlayer to the new position
                mediaPlayer.seekTo(newPosition);
            }
        });



        /**
         * This button moves the time of the song forwards for 5000ms
         */
        Button forwardsButton = (Button) findViewById(R.id.forwards_button);
        forwardsButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // if mediaPlayer is not initialized then do nothing (return)
                if(mediaPlayer == null) return;

                // get the new position for the mediaPlayer
                int newPosition = mediaPlayer.getCurrentPosition() + 5000;
                if(newPosition > mediaPlayer.getDuration()){
                    // if the position exceeds the duration of the mediaPlayer we need to put it
                    // to the end
                    newPosition = mediaPlayer.getDuration();
                }
                // set the mediaPlayer to the new position
                mediaPlayer.seekTo(newPosition);
            }
        });
    }



    /**
     * This method sets the volume to currentVolume
     * @param currentVolume the current volume of the audio file
     */
    private void setVolume(int currentVolume) {

        // if mediaPlayer is not initialized then do nothing (return)
        if(mediaPlayer == null) return;

        // logarithmic value for the volume
        float volume = 1 - (float) (Math.log(maxVolume-currentVolume)/Math.log(maxVolume));
        mediaPlayer.setVolume(volume, volume);
    }


    /**
     * This method puts the MainActivity in background and runs it
     * until  killed
     */
    protected void onStop() {

        // free up memory used by the MediaPlayer instance
        releaseMediaPlayer();

        super.onStop();
    }


    /**
     * This method releases the memory used by the MediaPlayer instance
     */
    private void releaseMediaPlayer() {

        // if mediaPlayer is already created
        if(mediaPlayer != null){

            mediaPlayer.release();

            mediaPlayer = null;
        }
        // abandon the request for audio focus because is no longer needed
        audioManager.abandonAudioFocus(audioFocusChangeListener);
    }
}
