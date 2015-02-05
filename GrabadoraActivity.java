package org.example.grabadora;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.IOException;


public class GrabadoraActivity extends Activity {

    private static final String LOG_TAG = "Grabadora";
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private static String fichero = Environment.getExternalStorageDirectory().getAbsolutePath()+"/audio.3gp";

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grabadora);
    }

    public void grabar(View view) {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(fichero);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Fallo en grabación");
        }
        mediaRecorder.start();
    }

    public void detenerGrabacion(View view) {
        mediaRecorder.stop();
        mediaRecorder.release();
    }

    public void reproducir(View view) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(fichero);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Fallo en reproducción");
        }
    }

    
}