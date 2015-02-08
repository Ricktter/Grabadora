package org.example.grabadora;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
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

    public void enviar(View view) throws IOException {

        Log.e(LOG_TAG, "Entro a enviar");

        new subir().execute();



    }

    private class subir extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

            String url = "http://192.168.0.4/prueba.php";

            File audio = new File(fichero);

            try {

                HttpPost httpost = new HttpPost(url);

                HttpClient client = new DefaultHttpClient();

                MultipartEntity entity = new MultipartEntity();

                entity.addPart("myAudioFile", new FileBody(audio));

                httpost.setEntity(entity);
                HttpResponse response = client.execute(httpost);
                HttpEntity resEntity = response.getEntity();

                if(resEntity != null)
                {
                    String responseStr = EntityUtils.toString(resEntity).trim();
                    Log.e(LOG_TAG,"response" + responseStr);
                }

            } catch (IOException e) {

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            Log.e(LOG_TAG, "Enviado");
        }



    }





}