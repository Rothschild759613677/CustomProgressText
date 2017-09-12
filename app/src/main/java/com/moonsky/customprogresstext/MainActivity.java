package com.moonsky.customprogresstext;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * 该项目主要实现带文本的进度条
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private int progressValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CustomProgress customProgress = (CustomProgress) findViewById(R.id.customProgressText);

        customProgress.setProgressListener(new CustomProgress.ProgressListener() {
            @Override
            public void complete() {
                Log.e(TAG, "complete: "+Thread.currentThread().getName());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "加载完成", Toast.LENGTH_LONG).show();
                    }
                });
                customProgress.post()

            }
        });

        customProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        do {
                            customProgress.setCurrentProgress(progressValue += 4);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        } while (progressValue < 100);

                    }
                }).start();
            }
        });
    }
}
