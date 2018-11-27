package com.deltorostudios.testtimer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * MainActivity - This activity serves two purposes. First, to test SharedPreferences by saving the number of times you've clicked the number.
 *                Second, to allow the user to enter a category to then be displayed in the Second Activity after clicking the button.
 */
public class MainActivity extends AppCompatActivity {


    // View references, int variable for click count and a String variable for sending data to the second activity
    private Button mainButton;
    private TextView numberTextView;
    private EditText categoryEditText;
    private int count;
    private String category;

    // Create constants for the SHARED PREFERENCES file, prefs mode and a key for storing a piece of data
    private static final String PREFS_FILE_MAIN = "MainPreferences";
    private static final int PREFS_MODE_MAIN = Context.MODE_PRIVATE;
    private static final String countKey = "redKey";
    private static final String categoryKey = "blueKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link references to relevant views
        mainButton = findViewById(R.id.mainButton);
        numberTextView = findViewById(R.id.numberTextView);
        categoryEditText = findViewById(R.id.categoryEditText);

        // Create an intent to be used to activate the second activity
        final Intent intent = new Intent(this, SecondActivity.class);

        // Set onClickListener for the "beam me up scotty" button to move to the second activity
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Make category equal to value in EditText and send it to/start SecondActivity
                category = categoryEditText.getText().toString();
                intent.putExtra("category_name", category);
                startActivity(intent);
            }
        });

        // Set onClickListener for the Number TextView
        numberTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Iterate count by 1
                count = count + 1;

                // Set number TextView equal to count
                numberTextView.setText(""+ count);
            }
        });

        // Set onCLickListener for the Category EditText
        categoryEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sets the editText to a blank string when you click it
                categoryEditText.setText("");
            }
        });

    }

    // Retrieves count or default(0) in Shared Preferences and update the number TextView accordingly
    @Override
    protected void onStart() {
        super.onStart();

        // Create sharedPreference object to pull any stored data and update the UI accordingly
        SharedPreferences prefs = getSharedPreferences(PREFS_FILE_MAIN, PREFS_MODE_MAIN);
        count = prefs.getInt(countKey, 0);
        category = prefs.getString(categoryKey, null);
        numberTextView.setText(""+ count);

        // Only update categoryEditText if category is not null(which it is by default)
        if (category != null) {
            categoryEditText.setText(category);
        }
    }

    // Stores count from Shared Preferences
    @Override
    protected void onStop() {
        super.onStop();

        // Get text from categoryEditText to save and reload
        category = categoryEditText.getText().toString();

        //Create sharedPreference object and SP.editor to put the count into sharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_FILE_MAIN, PREFS_MODE_MAIN);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(countKey, count);
        editor.putString(categoryKey, category);
        editor.apply();
    }

}