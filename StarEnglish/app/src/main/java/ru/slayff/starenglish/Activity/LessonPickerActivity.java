package ru.slayff.starenglish.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.slayff.starenglish.Activity.AddInfoActivity;
import ru.slayff.starenglish.R;


public class LessonPickerActivity extends ActionBarActivity {
    final String EXTRA_ADD_INFO_KEY = "KEY";
    //Key in Intent to show which .html info-file should be opened
    //Negative values mean info about lesson should be opened
    //Positive values mean additional info should be opened
    final String EXTRA_TEST_KEY = "TESTNUMBER"; //Key in Intent to show which test should be opened in TestActivity
    final String EXTRA_LESSON_PICKER_KEY = "LESNUMBER"; //Key in Intent to show which lesson's info should be opened here
    //
    public static final String APP_STATISTICS = "StarEnglishStatistics"; //Filename of full statistics
    SharedPreferences preferences; // object to get access to statistics
    private String BEST_ATTEMPT = "BEST_ATTEMPT_LES_";
    //
    ActionBar actionBar;
    Button buttoncontent;
    Button buttontesting;
    int KEYNUMBER;
    TextView textStatistics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_picker);
        buttoncontent = (Button) findViewById(R.id.bt_content);
        buttontesting = (Button) findViewById(R.id.bt_testing);
        textStatistics = (TextView) findViewById(R.id.text_stat);
        actionBar = getSupportActionBar();
        KEYNUMBER = (int) getIntent().getSerializableExtra(EXTRA_LESSON_PICKER_KEY); //Number of lesson has been chosen by user
        setView(KEYNUMBER);
    }

    /**
     * Activity is universal for any lessons, so app needs to set exact view depends on which lesson has been chosen
     * @param lessonNumber
     */
    private void setView(final int lessonNumber) {
        actionBar = getSupportActionBar();
        actionBar.setTitle("Урок " + lessonNumber);

        buttoncontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttoaddinfo = new Intent(LessonPickerActivity.this, AddInfoActivity.class);
                intenttoaddinfo.putExtra(EXTRA_ADD_INFO_KEY, -lessonNumber);
                startActivity(intenttoaddinfo);
            }
        });

        buttontesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttoTest = new Intent(LessonPickerActivity.this, TestActivity.class);
                intenttoTest.putExtra(EXTRA_TEST_KEY, lessonNumber);
                startActivity(intenttoTest);
                finish();
            }
        });

        preferences = getSharedPreferences(APP_STATISTICS, MODE_PRIVATE);
        float bestAttempt = preferences.getFloat(BEST_ATTEMPT + lessonNumber, -1);
        String generalStat;
        if (bestAttempt == -1 )
            generalStat = "Тест еще не пройден! Прочитайте материал урока и пройдите тест!";
        else {
            String fixedresult = String.format("%.0f", (bestAttempt * 100));
            generalStat = "Тест пройден на " + fixedresult + "%.";
        }

        String additionalStat = null;
        if (bestAttempt!=-1) {
            if (bestAttempt == 1.0) additionalStat = "Превосходно! Переходите к следующему уроку!";
            else if (bestAttempt >= 0.8)
                additionalStat = "Отлично! Переходите к следующему уроку или попробуйте улучшить свой результат!";
            else if (bestAttempt >= 0.6)
                additionalStat = "Хорошо, но можно и лучше! Переходите к следующему уроку или пройдите тест еще раз!";
            else if (bestAttempt >= 0.4)
                additionalStat = "Неплохо, но нужно повторить материал урока еще раз и пройти тест заново!";
            else
                additionalStat = "Очень плохо! Прочитайте материал урока еще раз и пройдите тест заново!";
        }
        if (additionalStat!=null) {
            String fullStat = generalStat + "\n" + additionalStat;
            textStatistics.setText(fullStat);
        }
        else
            textStatistics.setText(generalStat);
    }

    /**
     * methods allow user to go to statistics activity
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lesson_picker, menu);
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
