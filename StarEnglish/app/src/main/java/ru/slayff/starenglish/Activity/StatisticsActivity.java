package ru.slayff.starenglish.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;
import java.util.List;

import ru.slayff.starenglish.R;

public class StatisticsActivity extends ActionBarActivity {
    TextView textShortStat;
    ListView list_statistics;
    final public static int TOTAL_LESSONS_IN_APP = 5;
    //
    public static final String APP_STATISTICS = "StarEnglishStatistics"; //Filename of full statistics
    SharedPreferences preferences; // object to get access to statistics
    private String TOTAL_ATTEMPTS = "TOTAL_ATTEMPTS_LES_";
    private String BEST_ATTEMPT = "BEST_ATTEMPT_LES_";
    private String BEST_ATTEMPT_TIME = "BEST_ATTEMPT_TIME_LES_";
    private String DONE_LESSON = "DONE_LESSON_";
    private static final String TOTAL_LESSONS_DONE = "TOTAL_LESSONS_DONE";
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        textShortStat = (TextView) findViewById(R.id.text_completed_les);
        list_statistics = (ListView) findViewById(R.id.list_statistics);
        preferences = getSharedPreferences(APP_STATISTICS, MODE_PRIVATE);
        setView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistics, menu);
        return true;
    }

    /**
     * method allows user drop the statistics
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Сбросить статистику?")
                .setMessage("Вы действительно хотите сбросить всю статистику? Это действие невозможно отменить.")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }
                )
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dropAllStatistics();
                        Intent reopen = new Intent(StatisticsActivity.this, StatisticsActivity.class);
                        startActivity(reopen);
                        finish();
                    }
                });


        //noinspection SimplifiableIfStatement
        if (id == R.id.drop_statistic) {
            builder.create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * method sets the view: fills the listview with strings
     */
    private void setView() {
        int totalDoneTests = preferences.getInt(TOTAL_LESSONS_DONE, 0);
        String shortStat;
        if (totalDoneTests==1)
        shortStat = "Вы успешно завершили " + totalDoneTests + " урок из " + TOTAL_LESSONS_IN_APP;
        else if(totalDoneTests==2 || totalDoneTests==3 || totalDoneTests==4)
            shortStat = "Вы успешно завершили " + totalDoneTests + " урока из " + TOTAL_LESSONS_IN_APP;
        else
            shortStat = "Вы успешно завершили " + totalDoneTests + " уроков из " + TOTAL_LESSONS_IN_APP;
        textShortStat.setText(shortStat);
        ArrayAdapter<String> statisticsAdapter = new ArrayAdapter<String>(this, R.layout.result_list_item, getListOfStatistics());
        list_statistics.setAdapter(statisticsAdapter);
    }

    /**
     *
     * @return arraylist of statistics which will be opened in listview
     */
    private ArrayList<String> getListOfStatistics() {
        ArrayList<String> statisticsList = new ArrayList<>();
        for (int i = 0; i < TOTAL_LESSONS_IN_APP; i++) {
            statisticsList.add(i, getStatAboutLesson(i + 1));
        }
        return statisticsList;
    }

    /**
     *
     * @param lessonNumber shows which lesson's statistics should be shown
     * @return string which includes information about lesson
     */
    private String getStatAboutLesson(int lessonNumber) {
        String statistics = "Урок " + lessonNumber;
        float bestResult = preferences.getFloat(BEST_ATTEMPT+lessonNumber, -1);
        String fixedresult = String.format("%.0f", (bestResult * 100));
        String bestResultString = "Лучший результат при прохождении теста: "+fixedresult+"%";
        String date = preferences.getString(BEST_ATTEMPT_TIME+lessonNumber, "");
        int allAttempts = preferences.getInt(TOTAL_ATTEMPTS + lessonNumber, 0);
        if (bestResult==-1) statistics += "\n Не пройден!";
        else statistics += "\n " + bestResultString + "\n " + date + "\n Всего попыток: " + allAttempts;

        return statistics;
    }

    /**
     * method drops the statistics about all the lessons
     */
    private void dropAllStatistics() {
        for (int i = 1; i <= TOTAL_LESSONS_IN_APP; i++) {
            dropLessonStatistics(i);
        }
    }

    /**
     *
     * @param lessonNumber shows which lesson's statistic should be dropped
     */
    private void dropLessonStatistics(int lessonNumber) {
        Editor editor = preferences.edit();
        editor.putInt(TOTAL_ATTEMPTS + lessonNumber, 0);
        editor.putFloat(BEST_ATTEMPT + lessonNumber, -1);
        editor.putString(BEST_ATTEMPT_TIME+lessonNumber, "yyyy-MM-dd HH:mm");
        editor.putBoolean(DONE_LESSON + lessonNumber, false);
        editor.putInt(TOTAL_LESSONS_DONE, 0);
        editor.apply();
    }

}
