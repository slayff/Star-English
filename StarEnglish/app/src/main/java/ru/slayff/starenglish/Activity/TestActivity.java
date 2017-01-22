package ru.slayff.starenglish.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.*;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import ru.slayff.starenglish.QuizSystem.DataBaseHelper;
import ru.slayff.starenglish.QuizSystem.QHolder;
import ru.slayff.starenglish.QuizSystem.Question;
import ru.slayff.starenglish.QuizSystem.Session;
import ru.slayff.starenglish.R;

public class TestActivity extends ActionBarActivity {
    int counter; // count questions. counter needs to be <= Total questions in test
    Button btNext;
    TextView curQuestion; // Shows which question is shown now
    String userchoise;
    Session usersession;
    Toast hintToast;
    DataBaseHelper myDBHelper;
    int LESSON_NUMBER;
    final String EXTRA_TEST_KEY = "TESTNUMBER"; //Key in Intent which shows which test should be opened
    final String EXTRA_LESSON_PICKER_KEY = "LESNUMBER"; // Key in Intent which shows which lesson's info should be opened in LessonPickerActivity
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
        setContentView(R.layout.test_type_yn);
        btNext = (Button) findViewById(R.id.bt_next);

        myDBHelper = DataBaseHelper.getInstance(this);
        LESSON_NUMBER = (int) getIntent().getSerializableExtra(EXTRA_TEST_KEY);
        try {
            myDBHelper.createDataBase();
        } catch (IOException ioe) {
            Log.e("AppLog", "Unable to create database");
            throw new Error("Unable to create database");
        }
        try {
            myDBHelper.openDataBase();
        } catch(SQLException sqle) {
            Log.e("AppLog", "Unable to open DB");
            throw new Error("Unable to open DB");
        }

        usersession = new Session();
        counter = 0;
        QHolder qHolder = new QHolder(LESSON_NUMBER, myDBHelper);
        final List<Question> listofQ = qHolder.getQList();
        switch (listofQ.get(counter).getType()) {
            case "YNQ" : setViewYN(listofQ.get(counter), listofQ); break;
            case "CORQ" : setViewCOR(listofQ.get(counter), listofQ); break;
            case "MATCHQ" : setViewMATCH(listofQ.get(counter), listofQ); break;
            case "PICQ" :setViewPIC(listofQ.get(counter), listofQ); break;
        }
    }

    /**
     * setting view if question type is Yes/No
     */
    public void setViewYN(final Question question, final List<Question> listofQ) {
        setContentView(R.layout.test_type_yn);
        btNext = (Button) findViewById(R.id.bt_next);
        userchoise = "";
        TextView text_eng = (TextView) findViewById(R.id.text_eng_word);
        TextView text_rus = (TextView) findViewById(R.id.text_rus_word);
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupYN);
        curQuestion = (TextView) findViewById(R.id.text_num_q);
        curQuestion.setText("Вопрос "+(counter+1)+" из 10");
        text_eng.setText(question.getEng_word());
        text_rus.setText(question.getRus_word());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioY:
                        userchoise = "YES";
                        break;
                    case R.id.radioN:
                        userchoise = "NO";
                        break;
                }
            }
        });
        /**
         * alert "you haven't chosen anything"
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Вы не выбрали ответ!")
                .setMessage("Выберите один из вариантов ответа!")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();


        /**
         * Pressing NEXTButton -> Showing next question
         */
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId()==-1) alert.show();
                else {
                    boolean checkanswer = question.isYourAnswersCorrect(userchoise);
                        counter += 1;
                        String userAnswer = userchoise;
                        if (checkanswer) usersession.userIsCorrect(question, counter);
                        else usersession.userIsWrong(question, counter, userAnswer);

                    if (counter < 10) {
                        switch (listofQ.get(counter).getType()) {
                            case "YNQ":
                                setViewYN(listofQ.get(counter), listofQ);
                                break;
                            case "CORQ":
                                setViewCOR(listofQ.get(counter), listofQ);
                                break;
                            case "MATCHQ":
                                setViewMATCH(listofQ.get(counter), listofQ);
                                break;
                            case "PICQ":
                                setViewPIC(listofQ.get(counter), listofQ);
                                break;
                        }
                    }
                    if (counter == 10) {
                        setViewRESULT();
                    }
                }
            }
        });
    }

    /**
     * setting view if question type is "Correct input"
     */
    public void setViewCOR(final Question question, final List<Question> listofQ) {
        setContentView(R.layout.test_type_cor);
        btNext = (Button) findViewById(R.id.bt_next);
        Button bthint = (Button) findViewById(R.id.bt_hint);
        TextView textbase = (TextView) findViewById(R.id.text_base);
        curQuestion = (TextView) findViewById(R.id.text_num_q);
        curQuestion.setText("Вопрос "+(counter+1)+" из 10");

        final EditText edituser = (EditText) findViewById(R.id.edit_user);
        textbase.setText(question.getBase());
        /**
         * Alert "You haven't entered anything"
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Вы не ввели ответ!")
                .setMessage("Введите свой ответ!")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        /**
         * Hiding keyboard while pressing ENTER
         */
        edituser.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event!=null) && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) ) {
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(edituser.getApplicationWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        /**
         * Showing hint on pressing HintButton
         */
        bthint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hint = question.getHint();
                if (hintToast==null)
                    hintToast = Toast.makeText(getApplicationContext(), hint, Toast.LENGTH_LONG);
                    hintToast.show();

            }
        });
        /**
         * Pressing NEXTButton -> Showing next question
         */
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userAnswer = edituser.getText().toString();
                int l = userAnswer.trim().length();
                userAnswer =  userAnswer.toUpperCase();
                if (l==0) alert.show();
                //waiting until user writes at least something
                else {
                    hintToast=null;
                    boolean checkanswer = question.isYourAnswersCorrect(userAnswer);
                    counter += 1;
                    if (checkanswer) usersession.userIsCorrect(question, counter);
                    else usersession.userIsWrong(question, counter, userAnswer);
                    if (counter < 10) {
                        switch (listofQ.get(counter).getType()) {
                            case "YNQ":
                                setViewYN(listofQ.get(counter), listofQ);
                                break;
                            case "CORQ":
                                setViewCOR(listofQ.get(counter), listofQ);
                                break;
                            case "MATCHQ":
                                setViewMATCH(listofQ.get(counter), listofQ);
                                break;
                            case "PICQ":
                                setViewPIC(listofQ.get(counter), listofQ);
                                break;
                        }
                    }
                    if (counter == 10) {
                        setViewRESULT();
                    }
                }
            }
        });
    }

    /**
     * setting view if question type is "Match words"
     */
    public void setViewMATCH(final Question question, final List<Question> listofQ) {
        setContentView(R.layout.test_type_match);
        btNext = (Button) findViewById(R.id.bt_next);
        TextView text_rus = (TextView) findViewById(R.id.text_words_ru);
        TextView text_eng = (TextView) findViewById(R.id.text_words_en);
        final Spinner spin1 = (Spinner) findViewById(R.id.spinner_match_1);
        final Spinner spin2 = (Spinner) findViewById(R.id.spinner_match_2);
        final Spinner spin3 = (Spinner) findViewById(R.id.spinner_match_3);
        text_rus.setText(question.getWords_ru());
        text_eng.setText(question.getWords_en());
        curQuestion = (TextView) findViewById(R.id.text_num_q);
        curQuestion.setText("Вопрос "+(counter+1)+" из 10");

        /**
        * Alert "You haven't chosen options properly"
        */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ваш ответ некорректный")
                .setMessage("В этом вопросе не может быть два одинаковых ответа")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();

        /**
         * Pressing NEXTButton -> Showing next question
         */
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userans1 = spin1.getSelectedItem().toString();
                String userans2 = spin2.getSelectedItem().toString();
                String userans3 = spin3.getSelectedItem().toString();
                if (userans1.equals(userans2) || userans2.equals(userans3) || userans1.equals(userans3))
                    alert.show();
                else {
                    boolean checkanswer = question.isYourAnswersCorrect(userans1, userans2, userans3);
                    counter += 1;
                    String userAnswer = "1)"+userans1+" 2)"+userans2+" 3)"+userans3;
                    if (checkanswer) usersession.userIsCorrect(question, counter);
                    else usersession.userIsWrong(question, counter,userAnswer);
                    if (counter < 10) {
                        switch (listofQ.get(counter).getType()) {
                            case "YNQ":
                                setViewYN(listofQ.get(counter), listofQ);
                                break;
                            case "CORQ":
                                setViewCOR(listofQ.get(counter), listofQ);
                                break;
                            case "MATCHQ":
                                setViewMATCH(listofQ.get(counter), listofQ);
                                break;
                            case "PICQ":
                                setViewPIC(listofQ.get(counter), listofQ);
                                break;
                        }
                    }
                    if (counter == 10) {
                        setViewRESULT();
                    }
                }
            }
        });
    }

    /**
     * setting view if question type is "Match pictures"
     */
    public void setViewPIC(final Question question, final List<Question> listofQ) {
        setContentView(R.layout.test_type_pic);
        btNext = (Button) findViewById(R.id.bt_next);
        TextView textPicTitle = (TextView) findViewById(R.id.text_pic_title);
        ImageView pic_view1 = (ImageView) findViewById(R.id.imageView1);
        ImageView pic_view2 = (ImageView) findViewById(R.id.imageView2);
        ImageView pic_view3 = (ImageView) findViewById(R.id.imageView3);
        final Spinner spin1 = (Spinner) findViewById(R.id.spinner_pic_1);
        final Spinner spin2 = (Spinner) findViewById(R.id.spinner_pic_2);
        final Spinner spin3 = (Spinner) findViewById(R.id.spinner_pic_3);
        textPicTitle.setText(question.getPic_title());
        curQuestion = (TextView) findViewById(R.id.text_num_q);
        curQuestion.setText("Вопрос "+(counter+1)+" из 10");
        /**
         * putting images to imageview
         */
        try {
            // getting inputst
            InputStream mystream1 = getAssets().open(question.getPath_pic1());
            InputStream mystream2 = getAssets().open(question.getPath_pic2());
            InputStream mystream3 = getAssets().open(question.getPath_pic3());
            // loading as Drawable
            Drawable dr1 = Drawable.createFromStream(mystream1, null);
            Drawable dr2 = Drawable.createFromStream(mystream2, null);
            Drawable dr3 = Drawable.createFromStream(mystream3, null);
            // setting image in ImageView
            pic_view1.setImageDrawable(dr1);
            pic_view2.setImageDrawable(dr2);
            pic_view3.setImageDrawable(dr3);
        } catch (IOException ex) {
            pic_view1.setImageResource(R.mipmap.ic_launcher);
            pic_view2.setImageResource(R.mipmap.ic_launcher);
            pic_view3.setImageResource(R.mipmap.ic_launcher);
        }

        /**
         * Alert "You haven't chosen options properly"
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ваш ответ некорректный")
                .setMessage("В этом вопросе не может быть два одинаковых ответа")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        /**
         * Pressing NEXTButton -> Showing next question
         */
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userans1 = spin1.getSelectedItem().toString();
                String userans2 = spin2.getSelectedItem().toString();
                String userans3 = spin3.getSelectedItem().toString();
                if (userans1.equals(userans2) || userans2.equals(userans3) || userans1.equals(userans3))
                    alert.show();
                else    {
                    boolean checkanswer = question.isYourAnswersCorrect(userans1, userans2, userans3);
                counter += 1;
                String userAnswer = "1)"+userans1+" 2)"+userans2+" 3)"+userans3;
                if (checkanswer) usersession.userIsCorrect(question, counter);
                else usersession.userIsWrong(question, counter, userAnswer);
                if (counter < 10) {
                    switch (listofQ.get(counter).getType()) {
                        case "YNQ":
                            setViewYN(listofQ.get(counter), listofQ);
                            break;
                        case "CORQ":
                            setViewCOR(listofQ.get(counter), listofQ);
                            break;
                        case "MATCHQ":
                            setViewMATCH(listofQ.get(counter), listofQ);
                            break;
                        case "PICQ":
                            setViewPIC(listofQ.get(counter), listofQ);
                            break;
                    }
                }
                if (counter == 10) {
                    setViewRESULT();
                    }
                }
            }
        });
    }

    /**
     * setting view if counter == 10. All the questions are answered, so need to show results.
     */
    public void setViewRESULT() {
        setContentView(R.layout.test_result);
        TextView textShortResult = (TextView) findViewById(R.id.text_short_result);
        TextView textFullResult = (TextView) findViewById(R.id.text_full_result);
        ListView listresult = (ListView) findViewById(R.id.list_result);
        Button buttonRestart = (Button) findViewById(R.id.button_restart);
        Button buttonFinish = (Button) findViewById(R.id.button_finish);

        textFullResult.setText(usersession.getFullResult());
        textShortResult.setText(usersession.getShortResult());

        ArrayAdapter<String> statisticAdapter = new ArrayAdapter<String>(this, R.layout.result_list_item, usersession.getInformation());
        listresult.setAdapter(statisticAdapter);

        /**
         * restarting test (reopen activity)
         */
        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRestart = new Intent(TestActivity.this, TestActivity.class);
                intentRestart.putExtra(EXTRA_TEST_KEY, LESSON_NUMBER);
                startActivity(intentRestart);
                saveStatistics();
                myDBHelper.close();
                finish();

            }
        });

        /**
         * returning to previous activity (LessonPicker)
         */
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFinish = new Intent(TestActivity.this, LessonPickerActivity.class);
                intentFinish.putExtra(EXTRA_LESSON_PICKER_KEY, LESSON_NUMBER);
                startActivity(intentFinish);
                saveStatistics();
                myDBHelper.close();
                finish();
            }
        });
    }


    /**
     * include alert which asks if user is want to end test
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Прервать тестирование?")
                .setMessage("Вы действительно хотите завершить тест? Попытка не будет засчитана.")
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
                        Intent intent = new Intent(TestActivity.this, LessonPickerActivity.class);
                        intent.putExtra(EXTRA_LESSON_PICKER_KEY, LESSON_NUMBER);
                        startActivity(intent);
                        myDBHelper.close();
                        finish();
                    }
                });
        builder.create().show();
    }

    /**
     * method save all statistic about current attempt
     */
    private void saveStatistics() {
        preferences = getSharedPreferences(APP_STATISTICS, MODE_PRIVATE);
        TOTAL_ATTEMPTS += LESSON_NUMBER;
        BEST_ATTEMPT += LESSON_NUMBER;
        DONE_LESSON += LESSON_NUMBER;
        BEST_ATTEMPT_TIME += LESSON_NUMBER;
        int currentAttempts = preferences.getInt(TOTAL_ATTEMPTS, 0);
        float currentBestAttempt = preferences.getFloat(BEST_ATTEMPT, -1);
        int currentLessonsDone = preferences.getInt(TOTAL_LESSONS_DONE, 0);
        Editor editor = preferences.edit();
        editor.putInt(TOTAL_ATTEMPTS, currentAttempts + 1);
        if (usersession.getResult()>currentBestAttempt) {
            editor.putFloat(BEST_ATTEMPT, usersession.getResult());
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String date = simpleDateFormat.format(calendar.getTime());
            editor.putString(BEST_ATTEMPT_TIME, date);
        }
        if (usersession.isSuccessful()) {
            if (!(preferences.getBoolean(DONE_LESSON, false))) {
            editor.putBoolean(DONE_LESSON, true);
            editor.putInt(TOTAL_LESSONS_DONE, currentLessonsDone + 1); }
        }
        editor.apply();
    }

    /**
     * methods allow user to go to statistics activity
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
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
