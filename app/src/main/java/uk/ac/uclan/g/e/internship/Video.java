package uk.ac.uclan.g.e.internship;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class Video extends AppCompatActivity {

    private ImageButton button_sound;
    private  VideoView video;
    //http://stackoverflow.com/questions/26700315/videoview-ontouch-events-pause-resume-video-and-show-hide-mediacontroller-and
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        init();



    }

    private void init(){
        button_sound = (ImageButton) findViewById(R.id.button_video_state_sound);
        if(MainActivity.getStateSound()){
            button_sound.setImageResource(R.drawable.sound_active);
        }else{
            button_sound.setImageResource(R.drawable.icone_sound_mute);
        }
        video = (VideoView) findViewById(R.id.videoView);
        addClickListener();
        videoImplement();
        video.requestFocus();
        video.start(); // start de video
    }


    private void videoImplement(){
        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.videohead));
        // find de video path
        video.setMediaController(new MediaController(this));

        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // at the end of video
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(getApplicationContext(), "c'est fini...", Toast.LENGTH_LONG).show();
            }
        });

        video.setOnTouchListener(new View.OnTouchListener() {
            @Override // whenever the screen is touched while video is displayed
            public boolean onTouch(View v, MotionEvent event) {
                if (video.isPlaying()) { // video is playing
                    video.pause();
                } else {
                    video.start(); //  if it's already on pause state
                }

                return false;
            }
        });
    }


    private void addClickListener(){

        View.OnClickListener imgButtonHandler = new View.OnClickListener() {
            // create a View.Onclick Listener which will be use by button_sound
            public void onClick(View v) {
                AudioManager amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
                // audio managment
                if(MainActivity.getStateSound()){ // if the sound is enable
                    MainActivity.setSoundState(false); // disable sound
                    button_sound.setImageResource(R.drawable.icone_sound_mute);
                    // load image which coresponds at sound_inactive
                    amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                    // mute the sound (Music only)
                }else {
                    MainActivity.setSoundState(true);
                    amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                    // unmute the sound (Music only)
                    button_sound.setImageResource(R.drawable.sound_active);
                    // load the image sou
                }
            }
        };
        button_sound.setOnClickListener(imgButtonHandler);
    }
}
