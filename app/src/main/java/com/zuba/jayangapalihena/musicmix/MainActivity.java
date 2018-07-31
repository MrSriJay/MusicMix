package com.zuba.jayangapalihena.musicmix;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaplayer;
    private Button play;
    private Button next;
    private Button pre;
    private TextView righttime;
    private TextView lefttime;
    private SeekBar seekBar;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetUpUi();

        seekBar.setMax(mediaplayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    mediaplayer.seekTo(i);
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
                int currentPos=mediaplayer.getCurrentPosition();
                int duration=mediaplayer.getDuration();
                lefttime.setText(dateFormat.format(new Date(currentPos)));
                righttime.setText(dateFormat.format(new Date(duration)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    public void SetUpUi(){
        mediaplayer=new MediaPlayer();
        mediaplayer=MediaPlayer.create(getApplicationContext(),R.raw.oco);
        play=(Button) findViewById(R.id.play);
        next=(Button) findViewById(R.id.next);
        pre=(Button) findViewById(R.id.pre);
        righttime=(TextView) findViewById(R.id.rightTime);
        lefttime=(TextView) findViewById(R.id.leftTime);
        seekBar=(SeekBar) findViewById(R.id.seekBar);

        pre.setOnClickListener(this);
        next.setOnClickListener(this);
        play.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pre:

                break;

            case R.id.play:
                if(mediaplayer.isPlaying()){
                    pause();
                }else{
                    startMusic();
                }
                break;

            case R.id.next:
                break;
        }
    }

    public void pause(){
        if(mediaplayer!=null){
            mediaplayer.pause();
            play.setBackgroundResource(android.R.drawable.ic_media_play);
        }
    }

    public  void startMusic(){
        if(mediaplayer!=null){
            treadUpdate();
            mediaplayer.start();
            play.setBackgroundResource(android.R.drawable.ic_media_pause);
        }
    }

    public void treadUpdate(){

        thread=new Thread(){
            @Override
            public void run() {
               try{
                   while (mediaplayer!=null && mediaplayer.isPlaying()){
                       Thread.sleep(50);
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                            int newpos=mediaplayer.getCurrentPosition();
                            int newmax=mediaplayer.getDuration();
                            seekBar.setMax(newmax);
                            seekBar.setProgress(newpos);
                            lefttime.setText(String.valueOf(new SimpleDateFormat("mm:ss").format(new Date(mediaplayer.getCurrentPosition()))));
                            righttime.setText(String.valueOf(new SimpleDateFormat("mm:ss").format(new Date(mediaplayer.getCurrentPosition()))));

                           }
                       });
                   }
               }catch (InterruptedException e){
                   e.printStackTrace();
               }
            }
        };
        thread.start();
    }
}
