package com.nicare.ves.ui.main;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nicare.ves.BuildConfig;
import com.nicare.ves.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView tv = findViewById(R.id.tvVersion);
        tv.setText(String.format("v%s", BuildConfig.VERSION_NAME));
    }

}