package ru.slayff.starenglish.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import ru.slayff.starenglish.R;

public class SplashActivity extends ActionBarActivity {

    private Thread splashThread;
    private ImageView splashImage;
    private TextView splashText;
    Animation animFadeIn,animFadeOut,animScale,animCombo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashImage = (ImageView) findViewById(R.id.imageViewSplash);
        splashText = (TextView) findViewById(R.id.textViewSplash);
        animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        animScale = AnimationUtils.loadAnimation(this, R.anim.scale);
        animCombo = AnimationUtils.loadAnimation(this, R.anim.combo);
        splashImage.startAnimation(animFadeIn);
        splashText.startAnimation(animFadeIn);


        splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        //waiting 2sec or touching on screen
                        wait(2000);
                    }
                } catch (InterruptedException ex) {
                    throw new Error("Error in SplashActivity");
                }
                animCombo.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        splashImage.setVisibility(View.GONE);
                        Intent intenttoMain = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intenttoMain);
                        finish();

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                animFadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        splashText.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        splashImage.startAnimation(animCombo);
                        splashText.startAnimation(animFadeOut);
                    }
                });


            }
        };

        splashThread.start();
    }

    /**
     * method notifies the thread if user touch the screen, so the thread won't wait and continue working
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            synchronized (splashThread) {
                splashThread.notifyAll();
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
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
