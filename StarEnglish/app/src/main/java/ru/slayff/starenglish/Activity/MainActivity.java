package ru.slayff.starenglish.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import ru.slayff.starenglish.Activity.AddInfoActivity;
import ru.slayff.starenglish.Activity.LessonShowerActivity;
import ru.slayff.starenglish.R;


public class MainActivity extends ActionBarActivity {
    int keyAddInfo; //to specify exact .html file to be opened in AddInfoActivity
    final String EXTRA_ADD_INFO_KEY = "KEY";
    //Key in Intent to show which .html info-file should be opened
    //Negative values mean info about lesson should be opened
    //Positive values mean additional info should be opened
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent intenttoAddInfo = new Intent(this, AddInfoActivity.class);
        Button buttonLessons = (Button) findViewById(R.id.bt_lessons);
        Button buttonVerbs = (Button) findViewById(R.id.bt_verbs);
        Button buttonTenses = (Button) findViewById(R.id.bt_tenses);
        Button buttonAlphabet = (Button) findViewById(R.id.bt_alphabet);
        Button buttonStatistic = (Button) findViewById(R.id.bt_statistic);
        Button buttonAbout = (Button) findViewById(R.id.bt_about);

        buttonVerbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyAddInfo = 1;
                intenttoAddInfo.putExtra(EXTRA_ADD_INFO_KEY, keyAddInfo);
                startActivity(intenttoAddInfo);
            }
        });

        buttonAlphabet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyAddInfo = 3;
                intenttoAddInfo.putExtra(EXTRA_ADD_INFO_KEY, keyAddInfo);
                startActivity(intenttoAddInfo);
            }
        });

        buttonTenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyAddInfo = 2;
                intenttoAddInfo.putExtra(EXTRA_ADD_INFO_KEY, keyAddInfo);
                startActivity(intenttoAddInfo);
            }
        });

        buttonLessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttoLesShower = new Intent(MainActivity.this, LessonShowerActivity.class);
                startActivity(intenttoLesShower);
            }
        });
        buttonStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttoStatistics = new Intent(MainActivity.this, StatisticsActivity.class);
                startActivity(intenttoStatistics);
            }
        });

        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttoAbout = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intenttoAbout);
            }
        });
    }

    /**
     * Unnecessary methods - no need to go anywhere from main activity
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
