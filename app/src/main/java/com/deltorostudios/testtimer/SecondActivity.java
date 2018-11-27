package com.deltorostudios.testtimer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {
//    private CategoryViewModel DetailViewModel;

    // FileName, Mode and Keys for sharedPreferences
    private static final String PREFS_FILE = "MyPreferences";
    private static final int PREFS_MODE = Context.MODE_PRIVATE;
    private static final String timerIsRunningKey = "blueKey";
    private static final String storedTimeKey = "greenKey";
    private static final String TAG = "boo";
    String categoryString;



    // References for buttons, timerView and creating a Handler for the runnable
    private Button startButton, pauseButton, resetButton, commitButton;
    private TextView categoryView, timerView;
    private Handler handler;

    // Variables for timer code
    //           // Non-UI variables          //Variables that affect the UI
    private long startTime, millisecondsLong, updatedMillisecondsLong, timeBeforePause, storedTime;

    // Variable for saving timer state(running or paused) into shared preferences
    private Boolean timerIsPaused = false;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Initiate buttons/textViews and Handler
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        resetButton = findViewById(R.id.resetButton);
        commitButton = findViewById(R.id.commitButton);
        timerView = findViewById(R.id.timerView);
        categoryView = findViewById(R.id.categoryView);
        handler = new Handler();

        // Get intent that gives use the category
        categoryString = getIntent().getStringExtra("category_name");
        // Set categoryView as the proper category
        categoryView.setText(categoryString);

        // Set Pause/Reset/Commit to be setEnabled(false) on startup
        pauseButton.setEnabled(false);
        resetButton.setEnabled(false);
        commitButton.setEnabled(false);


        // Method that sets OnClickListeners for all four buttons(better then XML because we can do .setEnabled)
        ButtonsOnClick();
    }



    /**
     * onStart
     */

    @Override
    protected void onStart() {
        super.onStart();

        /* SharedPreferences prefs = getSharedPreferences(PREFS_FILE, PREFS_MODE);

        // Get time value from preferences or assign it the default
        storedTime = prefs.getLong(storedTimeKey, 0);
        timerIsPaused = prefs.getBoolean(timerIsRunningKey, false);

        if (timerIsPaused) {
            // Enable start, disable pause
            startButton.setEnabled(true);
            pauseButton.setEnabled(false);

            // Format storedTime into 00:00:00 H/M/S and updated TimerView accordingly
            formatMillisIntoHMS(storedTime);

            Toast.makeText(this, "Timer is paused!", Toast.LENGTH_SHORT).show();
        } else if (!timerIsPaused && storedTime > 0) {
            handler.post(runForrestRun);

            startTime = SystemClock.elapsedRealtime();
            timerIsPaused = true;

        } */
    }


    /**
     * onStop
     */
    @Override
    protected void onStop() {
        super.onStop();



        /* // Make storedTime accurate
        storedTime = millisecondsLong + timeBeforePause;

        //Save the flag
        SharedPreferences prefs = getSharedPreferences(PREFS_FILE, PREFS_MODE);
        SharedPreferences.Editor editor = prefs.edit();

        // Write the time values to shared prefs
        editor.putLong(storedTimeKey, storedTime);
        editor.putBoolean(timerIsRunningKey, timerIsPaused);
        editor.apply();  */
    }



    public Runnable runForrestRun = new Runnable() {
        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        public void run() {


            // Increment the timer
            millisecondsLong = SystemClock.elapsedRealtime() - startTime;

            //Update the millis to count for the time before pause
            updatedMillisecondsLong = millisecondsLong + timeBeforePause + storedTime;



            // Format updatedMillisecondsLong into 00:00:00 H/M/S and update timer accordingly
            formatMillisIntoHMS(updatedMillisecondsLong);

            // DJ play my song one more time
            handler.post(runForrestRun);
        }
    };



    /**
     *  Buttons start/pause/reset/commit onClickListeners
     */
    public void ButtonsOnClick() {

        // Start
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set buttons enabled states so start is false
                startButton.setEnabled(false);
                pauseButton.setEnabled(true);
                resetButton.setEnabled(true);
                commitButton.setEnabled(true);

                // Boolean timerIsRunning to true
                timerIsPaused = true;

                // Get the startTime or a new startTime after pause button
                startTime = SystemClock.elapsedRealtime();

                // Run the timer thread
                handler.post(runForrestRun);

            }
        });

        // Pause
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Terminate timer runnable
                handler.removeCallbacks(runForrestRun);

                // Boolean timerIsPaused to false
                timerIsPaused = false;

                // Enable start, disable pause
                startButton.setEnabled(true);
                pauseButton.setEnabled(false);

                // timeBeforePause is the total millis in the now paused timer
                timeBeforePause = timeBeforePause + SystemClock.elapsedRealtime() - startTime;

            }
        });

        //Reset
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Stops the runnable
                handler.removeCallbacks(runForrestRun);

                // Reset start button to Enabled in case it isn't
                startButton.setEnabled(true);
                pauseButton.setEnabled(false);
                resetButton.setEnabled(false);
                commitButton.setEnabled(false);

                // Make variables that affect UI equal to 0
                updatedMillisecondsLong = 0;
                timeBeforePause = 0;
                storedTime = 0;
                timerIsPaused = false;


                //Sets the timer to 00:00:00
                formatMillisIntoHMS(updatedMillisecondsLong);


            }
        });

        //Commit
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo: Stop timer and save it in the database
                Toast.makeText(SecondActivity.this, "commit doesn't work rn", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * formatMillisIntoHMS - formats out time from millisecondsLong into 00:00:00
     */
    public void formatMillisIntoHMS(Long milliseconds) {

        int seconds, minutes, hours;

        //Turn millisecondsLong into ints and h/m/s
        seconds = (int) (milliseconds/1000);
        minutes = seconds/60;
        hours = minutes/60;
        // Mod those ints to keep them from 0-59
        seconds = seconds % 60;
        minutes = minutes % 60;
        hours = hours % 60;
        // Update the timer and do String.format magic
        timerView.setText(String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));

    }


}
