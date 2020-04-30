package com.goqing.timetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//import com.idescout.sql.SqlScoutServer;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        for SqlScout live debug
//        SqlScoutServer.create(this, getPackageName());

        int background_color_button = getResources().getColor(R.color.button_color);
        Button buttonStart = findViewById(R.id.button_StartTrack);
        buttonStart.setBackground(MyUtility.getRoundRect(background_color_button));
        Button buttonWatchRecord = findViewById(R.id.button_watchrecord);
        buttonWatchRecord.setBackground(MyUtility.getRoundRect(background_color_button));
    }

    public void enterWatchRecord(View view) {
        Intent goToRecord = new Intent();
        goToRecord.setClass(this, RecordActivity.class);
        startActivity(goToRecord);
    }

    public void enterStartTrack(View view) {
        Intent goToTrack = new Intent();
        goToTrack.setClass(this, TrackActivity.class);
        startActivity(goToTrack);
    }
}

