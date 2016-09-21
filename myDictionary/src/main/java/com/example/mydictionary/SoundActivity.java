package com.example.mydictionary;


import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SoundActivity extends Activity {
    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    private boolean flag = true;
    private myThread thread = null;
    boolean mStartRecording = true;
    Button b;
    TextView tv;
    ProgressBar pb;
    Handler h;
    AudioManager am;
    int volume;
    int volumeMax;
    static double index = 27;

    private void onRecord(boolean start) {
        if (start) {
            flag = true;
            startRecording();
        } else {
            if (thread != null)
                thread.exit();
            stopRecording();
        }
    }

    
    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // mRecorder.setOutputFile("/dev/null");
        mRecorder.setOutputFile(mFileName);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        mRecorder.start();
        thread = new myThread();
        thread.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    class RecordButton extends Button {
        boolean mStartRecording = true;
        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    public void AudioRecordTest() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        AudioRecordTest();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sound);
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumeMax = am.getStreamMaxVolume(AudioManager.STREAM_RING);
        volume = am.getStreamVolume(AudioManager.STREAM_RING);
        pb.setProgress(100*volume/volumeMax);
        tv = (TextView) findViewById(R.id.tv);
        h = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                Double d = (Double) msg.obj;
                tv.setText("" + d);
                if ((d - index) > 5) {
                    index = d;
                    am.adjustVolume(AudioManager.ADJUST_RAISE, 0);
                    volume = am.getStreamVolume(AudioManager.STREAM_RING);
                    pb.setProgress(100*volume/volumeMax);
                }
                if ((index - d) > 5) {
                    index = d;
                    am.adjustVolume(AudioManager.ADJUST_LOWER, 0);
                    pb.setProgress(100*volume/volumeMax);
                }
            }

        };
        b = (Button) findViewById(R.id.button1);
        b.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onRecord(mStartRecording);
                if (mStartRecording) {
                    b.setText("停止监听");
                } else {
                    b.setText("开始监听");
                }
                mStartRecording = !mStartRecording;
            }

        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.KEYCODE_BACK) {
            if (thread != null)
                thread.exit();
            stopRecording();
        }
        return super.onKeyDown(keyCode, event);
    }

    private class myThread extends Thread {
        myThread() {
        }

        public void exit() {
            flag = false;
            while (!flag)
                ;// nice`!
        }

        public void run() {
            while (flag) {
                int x = mRecorder.getMaxAmplitude();
                if (x != 0) {
                    double f = 10 * Math.log(x) / Math.log(10);
                    //Log.d(LOG_TAG, "" + f);
                    Message m = new Message();
                    m.obj = f;
                    h.sendMessage(m);
                }
            }
            flag = true;
        }
    }
}
