package ru.slayff.starenglish.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import ru.slayff.starenglish.Activity.LessonPickerActivity;
import ru.slayff.starenglish.R;


public class LessonShowerActivity extends ActionBarActivity {
    final String EXTRA_LESSON_PICKER_KEY = "LESNUMBER"; //Key in Intent to show which lesson's info should be opened in LessonPickerActivity
    //
    public static final String APP_STATISTICS = "StarEnglishStatistics"; //Filename of full statistics
    SharedPreferences preferences; // object to get access to statistics
    private String DONE_LESSON = "DONE_LESSON_";
    //
    Button buttonlesson1;
    Button buttonlesson2;
    Button buttonlesson3;
    Button buttonlesson4;
    Button buttonlesson5;
    Button buttonlesson6;
    Button buttonlesson7;
    Button buttonlesson8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessonshower);

        setButtons();
        setButtonsColor();
    }

    private void setButtons() {
        buttonlesson1 = (Button) findViewById(R.id.bt_les1);
        buttonlesson2 = (Button) findViewById(R.id.bt_les2);
        buttonlesson3 = (Button) findViewById(R.id.bt_les3);
        buttonlesson4 = (Button) findViewById(R.id.bt_les4);
        buttonlesson5 = (Button) findViewById(R.id.bt_les5);
        buttonlesson6 = (Button) findViewById(R.id.bt_les6);
        buttonlesson7 = (Button) findViewById(R.id.bt_les7);
        buttonlesson8 = (Button) findViewById(R.id.bt_les8);

        buttonlesson1.setOnClickListener(getButtonListener(1));
        buttonlesson2.setOnClickListener(getButtonListener(2));
        buttonlesson3.setOnClickListener(getButtonListener(3));
        buttonlesson4.setOnClickListener(getButtonListener(4));
        buttonlesson5.setOnClickListener(getButtonListener(5));
    }

    /**
     * @param lessonNumber >1
     * @return true if user has completed previous lesson
     */
    private boolean isButtonEnabled(int lessonNumber) {
        preferences = getSharedPreferences(APP_STATISTICS, MODE_PRIVATE);
        return preferences.getBoolean(DONE_LESSON + (lessonNumber - 1), false);
    }

    /**
     * if user has completed the test the next button should be activated
     */
    @Override
    protected void onResume() {
        super.onResume();
        buttonlesson2.setEnabled(isButtonEnabled(2));
        buttonlesson3.setEnabled(isButtonEnabled(3));
        buttonlesson4.setEnabled(isButtonEnabled(4));
        buttonlesson5.setEnabled(isButtonEnabled(5));
        setButtonsColor();
    }

    /**
     * the same method for all the buttons
     * @param lesson shows which lesson should be opened in next activity
     */
    private View.OnClickListener getButtonListener(final int lesson) {
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttopicker = new Intent(LessonShowerActivity.this, LessonPickerActivity.class);
                intenttopicker.putExtra(EXTRA_LESSON_PICKER_KEY, lesson);
                startActivity(intenttopicker);
            }
        };
        return buttonClickListener;
    }

    /**
     * set correct colors for all the buttons
     */
    private void setButtonsColor() {
        setCorrectFontCollor(buttonlesson1);
        setCorrectFontCollor(buttonlesson2);
        setCorrectFontCollor(buttonlesson3);
        setCorrectFontCollor(buttonlesson4);
        setCorrectFontCollor(buttonlesson5);
        setCorrectFontCollor(buttonlesson6);
        setCorrectFontCollor(buttonlesson7);
        setCorrectFontCollor(buttonlesson8);
    }

    /**
     * set correct color: if button is disabled it's better to see black rather than white font
     */
    private void setCorrectFontCollor(Button bt) {
        if (!bt.isEnabled()) {
            bt.setTextColor(Color.BLACK);
        } else bt.setTextColor(Color.WHITE);
    }

    /**
     * allow user to go to statistics activity
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lesson_shower, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent stat_intent = new Intent(this, StatisticsActivity.class);
            startActivity(stat_intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
