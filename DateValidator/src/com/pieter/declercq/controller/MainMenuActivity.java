package com.pieter.declercq.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pieter.declercq.datevalidator.R;

/**
 * Created by Pieter on 18/12/2014.
 */
public class MainMenuActivity extends Activity {

    private String mDatabaseFile;

    private String mExpiryListFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button mCategoryPicker = (Button) findViewById(R.id.new_list_button);
        mCategoryPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, CategoryPickerActivity.class);
                startActivity(i);
            }
        });

        Button mExpiryListSummary = (Button) findViewById(R.id.summary_list_button);
        mExpiryListSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, ExpiryListActivity.class);
                startActivity(i);
            }
        });

    }

}
