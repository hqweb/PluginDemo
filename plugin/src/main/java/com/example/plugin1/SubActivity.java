package com.example.plugin1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;

import com.example.plugin1.utils.ResourcesManager;


public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppCompat);

        setContentView(R.layout.activity_main);
    }

    @Override
    public Resources getResources() {
        return ResourcesManager.mNowResources;
    }

}