package com.hackathon4.mash.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.hackathon4.mash.R;

/**
 * Created by stephenquick on 8/23/14.
 */
public class ActivitySplash extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final int splashDisplay = 4000;
        Thread welcomeThread = new Thread() {

            int wait = 0;

            @Override
            public void run() {
                try {
                    super.run();
                    while (wait < splashDisplay) {
                        sleep(100);
                        wait += 100;
                    }
                } catch (Exception e) {
                    System.out.println("exception:" + e);
                } finally {
                    startActivity(new Intent(ActivitySplash.this, ActivityMain.class));
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}
