package com.rrmsense.radiostream.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.rrmsense.radiostream.R;
import com.rrmsense.radiostream.models.Storage;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonClearRecent;
    Button buttonClearFavourite;
    Button buttonClearAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        buttonClearFavourite = (Button) findViewById(R.id.button_clear_favourite);
        buttonClearFavourite.setOnClickListener(this);
        buttonClearRecent = (Button) findViewById(R.id.button_clear_recent);
        buttonClearRecent.setOnClickListener(this);
        buttonClearAll = (Button) findViewById(R.id.button_clear_all);
        buttonClearAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_clear_favourite:
                Storage.removeAllFavourite(getApplication());

                break;
            case R.id.button_clear_recent:
                Storage.RemoveAllRecent(getApplication());
                break;
            case R.id.button_clear_all:
                Storage.removeAll(getApplication());

                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
