package edu.ib;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int seconds = 0;
    private double volume = 0;
    private boolean running;
    private String result;
    private double h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
        }

        runTimer();
    }//end of onCreate

    public void onClickStart(View view) {
        final EditText hInput = (EditText) findViewById(R.id.enterHValue);
        boolean doStart = true;

        try {
            String input = String.valueOf(hInput.getText());
            h = Double.valueOf(input);

            if (h <= 0)
                throw new IllegalArgumentException();
        } catch (NumberFormatException e) {
            result = "Please enter the value";
            hInput.setText(result);
            doStart = false;

        } catch (IllegalArgumentException e) {
            result = "Incorrect value";
            hInput.setText(result);
            doStart = false;
        }
        if (doStart)
            running = true;
    }//end of onClickStart

    public void onClickStop(View view) {
        running = false;
    }//end of onClickStop

    public void onClickReset(View view) {
        final EditText hInput = (EditText) findViewById(R.id.enterHValue);
        running = false;
        seconds = 0;
        volume = 0;
        result = null;
        hInput.setText(result);
    }//end of onCLickReset

    private void runTimer() {

        final TextView timeView = (TextView) findViewById(R.id.time_view);
        final TextView volumeView = (TextView) findViewById(R.id.textViewVolume);

        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {

                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                double volumeSecond = (0.8 * Math.PI * Math.pow(0.2, 2)) / 4 * Math.sqrt((2 * 10 * h * (780 - 1.2)) / 1.2);

                String volumeFormat = String.format(Locale.getDefault(), "%.2f", volume);
                volumeView.setText(volumeFormat);

                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
                timeView.setText(time);

                if (running) {
                    seconds++;
                    volume += volumeSecond;
                }

                handler.postDelayed(this, 1000);

            }//end of run
        });

    }//end of runTimer


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
    }//end of onSaveInstanceState
}//end of class
