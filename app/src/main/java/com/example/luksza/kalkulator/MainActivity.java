package com.example.luksza.kalkulator;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        xEditText = (EditText) findViewById(R.id.xEditText);
        yEditText = (EditText) findViewById(R.id.yEditText);
        wynikEditText = (EditText) findViewById(R.id.wynikEditText);
        progresBar = (ProgressBar) findViewById(R.id.progressBar);

    }


    public EditText xEditText;
    public EditText yEditText;
    public EditText wynikEditText;
    public ProgressBar progresBar;



    private class PiComputeTask extends AsyncTask<Void, Integer, Double> {
        protected Double doInBackground(Void... voids) {

            double wSrodku=0;

            Random los = new Random();
            double x, y;

            for(int i = 0;i<100;i++) {
                for (int k = 0; k < 10000; k++)
                {
                    x = los.nextDouble();
                    y = los.nextDouble();
                    if(x*x+y*y<1.0) wSrodku++;
                }
                    publishProgress(i);
            }
            return  wSrodku * 4.0/1e6;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //super.onProgressUpdate(values);
            progresBar.setProgress(values[0]);

        }

        protected void onPostExecute(Double result) {
            xEditText.setText(String.valueOf(result));
        }
    }


    public void piButtonClick(View v) {

        //xEditText.setText("okokokooko");
        progresBar.setProgress(0);
        new PiComputeTask().execute();

       // xEditText.setText("KKKKKKKKKKKKKKKKKK");

    }




    public void addButtonClick(View v) {
        if (mBound){

            double x = Double.valueOf(String.valueOf(xEditText.getText()));
            double y = Double.valueOf(String.valueOf(yEditText.getText()));
            double wynik =(logicService.add(x,y));
            wynikEditText.setText(String.valueOf(wynik));
        }
    }

    public void subButtonClick(View v) {
        if (mBound){

            double x = Double.valueOf(String.valueOf(xEditText.getText()));
            double y = Double.valueOf(String.valueOf(yEditText.getText()));
            double wynik =(logicService.sub(x,y));
            wynikEditText.setText(String.valueOf(wynik));
        }
    }
    public void mulButtonClick(View v) {
        if (mBound){

            double x = Double.valueOf(String.valueOf(xEditText.getText()));
            double y = Double.valueOf(String.valueOf(yEditText.getText()));
            double wynik =(logicService.mul(x,y));
            wynikEditText.setText(String.valueOf(wynik));
        }
    }
    public void divButtonClick(View v) {
        if (mBound){

            double x = Double.valueOf(String.valueOf(xEditText.getText()));
            double y = Double.valueOf(String.valueOf(yEditText.getText()));
            double wynik =(logicService.div(x,y));
            wynikEditText.setText(String.valueOf(wynik));
        }
    }



    LogicService logicService;
    boolean mBound = false;
    private ServiceConnection logicConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            LogicService.LocalBinder binder = (LogicService.LocalBinder) service;
            logicService = binder.getService();
            mBound = true;
            Toast.makeText(MainActivity.this, "Logic Service Connected!",
                    Toast.LENGTH_SHORT).show();
        }
        public void onServiceDisconnected(ComponentName className) {
            logicService = null;
            mBound = false;
            Toast.makeText(MainActivity.this, "Logic Service Disconnected!",
                    Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound) {
            this.bindService(new Intent(MainActivity.this,LogicService.class),
                    logicConnection,Context.BIND_AUTO_CREATE);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            mBound = false;
            this.unbindService(logicConnection);
        }
    }



}
