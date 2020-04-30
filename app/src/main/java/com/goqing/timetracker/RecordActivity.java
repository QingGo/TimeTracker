package com.goqing.timetracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class RecordActivity extends Activity {

    AppDatabase database;
    TimeRecordDao timeRecordDao;
    TextView textViewStartTime, textViewEndTime;
    DateFormat dfYMDHMS, dfYMD;
    RecordActivity thisInstance;
    LinearLayout linearLayoutText;
    Long startTimeStamp, endTimeStamp;
    TextView textViewDesc;
    TextView textViewTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        database = AppDatabase.getInstance(getApplicationContext());
        timeRecordDao = database.userDao();
        textViewStartTime = findViewById(R.id.textViewStartTime);
        textViewEndTime = findViewById(R.id.textViewEndTime);
        dfYMDHMS = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dfYMD = new SimpleDateFormat("yyyy/MM/dd");
        thisInstance = this;
        linearLayoutText = findViewById(R.id.LinearLayout_Text);

        Button buttonStartTime = findViewById(R.id.button_SelectStartTime);
        Button buttonEndTime = findViewById(R.id.button_SelectEndTime);
        int background_color_button = getResources().getColor(R.color.button_color);
        buttonStartTime.setBackground(MyUtility.getRoundRect(background_color_button));
        buttonEndTime.setBackground(MyUtility.getRoundRect(background_color_button));

        endTimeStamp = new Date().getTime();
        // one day ago
        startTimeStamp = endTimeStamp - 24 * 60 * 60 * 1000;
        textViewStartTime.setText(dfYMDHMS.format(new Date(startTimeStamp)));
        textViewEndTime.setText(dfYMDHMS.format(new Date(endTimeStamp)));

        updateList();
    }

    public void updateList(){
        // clean all before add child views
        // Only the original thread that created a view hierarchy can touch its views.
        linearLayoutText.removeAllViews();
        // room默认在ui线程运行会报错,不知为何用Handler也不行
        new Thread() {
            @Override
            public void run() {

                List<TimeRecord> records = timeRecordDao.loadBetween(startTimeStamp, endTimeStamp);
                for (TimeRecord record : records) {

                    Date recordDate = new Date(record.timestamp);
                    long totalTimeSeconds = record.time_spend;
                    long hours = totalTimeSeconds / 3600;
                    long minutes = (totalTimeSeconds % 3600) / 60;
                    long secs = totalTimeSeconds % 60;

                    String timeSpendStr = String.format("using %d:%02d:%02d at time %s",
                            hours, minutes, secs, dfYMDHMS.format(recordDate));

                    textViewDesc = new TextView(thisInstance);
                    textViewDesc.setText(record.desc);
                    textViewDesc.setTextSize(30);
                    textViewDesc.setBackgroundColor(getResources().getColor(R.color.white));
                    textViewDesc.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));


                    textViewTime = new TextView(thisInstance);
                    textViewTime.setText(timeSpendStr);
                    textViewTime.setTextSize(18);
                    textViewTime.setBackgroundColor(getResources().getColor(R.color.little_dark));
                    textViewTime.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    linearLayoutText.addView(textViewDesc);
                    linearLayoutText.addView(textViewTime);

                }
            }
        }.start();
    }




    public void selectStartTime(View view) {
        final Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog  StartTime = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth, 0, 0,0);
                textViewStartTime.setText(dfYMDHMS.format(newDate.getTime()));
                startTimeStamp = newDate.getTimeInMillis();
                updateList();
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        StartTime.show();
    }

    public void selectEndTime(View view) {
        final Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog StartTime = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth, 0, 0,0);
                textViewEndTime.setText(dfYMDHMS.format(newDate.getTime()));
                endTimeStamp = newDate.getTimeInMillis();
                updateList();
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        StartTime.show();
    }
}
