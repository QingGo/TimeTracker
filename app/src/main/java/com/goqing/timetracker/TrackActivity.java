package com.goqing.timetracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.util.Locale;

public class TrackActivity extends Activity {


    boolean isTiming;
    long totalTimeSeconds;
    TextView textViewTotalTime;
    Button buttonStartOrPauseTiming;
    Button buttonResetTiming;
    Button buttonFinishTiming;

    AppDatabase database;
    TimeRecordDao timeRecordDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        isTiming = false;
        totalTimeSeconds = 0;
        int background_color_button = getResources().getColor(R.color.button_color);

        textViewTotalTime = findViewById(R.id.textView_TotalTime);
        buttonStartOrPauseTiming = findViewById(R.id.button_StartOrPauseTiming);
        buttonResetTiming = findViewById(R.id.button_ResetTiming);
        buttonFinishTiming = findViewById(R.id.button_FinishTiming);

        buttonStartOrPauseTiming.setBackground(MyUtility.getRoundRect(background_color_button));
        buttonResetTiming.setBackground(MyUtility.getRoundRect(background_color_button));
        buttonFinishTiming.setBackground(MyUtility.getRoundRect(background_color_button));

        database = AppDatabase.getInstance(getApplicationContext());
        timeRecordDao = database.userDao();

        runTimer();
    }

    private void runTimer() {
        // Creates a new Handler
        // 在多线程的应用场景中，将工作线程中需更新UI的操作信息 传递到UI主线程，
        final Handler handler = new Handler();

        // Call the post() method,
        // passing in a new Runnable.
        // The post() method processes
        // code without a delay,
        // so the code in the Runnable
        // will run almost immediately.
        handler.post(new Runnable() {
            @Override

            public void run()
            {
                long hours = totalTimeSeconds / 3600;
                long minutes = (totalTimeSeconds % 3600) / 60;
                long secs = totalTimeSeconds % 60;

                // Format the seconds into hours, minutes,
                // and seconds.
                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);

                // Set the text view text.
                textViewTotalTime.setText(time);

                // If running is true, increment the
                // seconds variable.
                if (isTiming) {
                    totalTimeSeconds++;
                }

                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void resetTiming(View view) {
        AlertDialog.Builder confirmReset = new AlertDialog.Builder(this);
        confirmReset.setPositiveButton(R.string.confirm_yes, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                isTiming = false;
                totalTimeSeconds = 0;
                buttonStartOrPauseTiming.setText("START");
            }
        });
        confirmReset.setNegativeButton(R.string.confirm_no, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.dismiss();
            }
        });
        confirmReset.setMessage("Are you sure to reset the time?");
        confirmReset.setTitle("Hint");
        confirmReset.show();
    }

    public void finishTiming(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton(R.string.confirm_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isTiming = false;
                String recordText = input.getText().toString();
                long timestampNow = new Date().getTime();
                final TimeRecord timeRecord = new TimeRecord(timestampNow, totalTimeSeconds, recordText);

                // room默认在ui线程运行会报错
                new Thread() {
                    @Override
                    public void run() {
                        //这里写入子线程需要做的工作
                        timeRecordDao.insert(timeRecord);
                    }
                }.start();
                totalTimeSeconds = 0;
                buttonStartOrPauseTiming.setText("START");
            }
        });
        builder.setNegativeButton(R.string.confirm_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setMessage("What have you done during this time?");
        builder.show();

    }

    public void startOrPauseTiming(View view) {
        if (isTiming) {
            // pause
            isTiming = false;
            buttonStartOrPauseTiming.setText("START");
        } else {
            isTiming = true;
            buttonStartOrPauseTiming.setText("STOP");
        }
    }
}
