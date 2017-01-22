package ru.slayff.starenglish.Activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import ru.slayff.starenglish.R;


public class AddInfoActivity extends ActionBarActivity {
    int keyInfo; // getting extra from intent to know which .html should be opened
    final String EXTRA_ADD_INFO_KEY = "KEY";
    //Key in Intent to show which .html info-file should be opened
    //Negative values mean info about lesson should be opened
    //Positive values mean additional info should be opened
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        WebView webViewAddInfo = (WebView) findViewById(R.id.webViewAddInfo);
        keyInfo = (int) getIntent().getSerializableExtra(EXTRA_ADD_INFO_KEY);
        switch (keyInfo) {
            case 1 : webViewAddInfo.loadUrl("file:///android_asset/VerbsTable.html"); break;
            case 2 : webViewAddInfo.loadUrl("file:///android_asset/TensesTable.html"); break;
            case 3: webViewAddInfo.loadUrl("file:///android_asset/Alphabet.html"); break;
            case -1: webViewAddInfo.loadUrl("file:///android_asset/Lesson1.html"); break;
            case -2: webViewAddInfo.loadUrl("file:///android_asset/Lesson2.html"); break;
            case -3: webViewAddInfo.loadUrl("file:///android_asset/Lesson3.html"); break;
            case -4: webViewAddInfo.loadUrl("file:///android_asset/Lesson4.html"); break;
            case -5: webViewAddInfo.loadUrl("file:///android_asset/Lesson5.html"); break;
        }
    }

    /**
     * Unnecessary methods - no need to go anywhere from addinfo activity
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_add_info, menu);
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
