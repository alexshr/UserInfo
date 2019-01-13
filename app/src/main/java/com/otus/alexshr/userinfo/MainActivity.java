package com.otus.alexshr.userinfo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements Manager {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            showInputFragment();
        }
    }

    @Override
    public void showInputFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new InputFragment())
                .commit();
    }

    @Override
    public void showDisplayFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new DisplayFragment())
                .addToBackStack(null)
                .commitNow();
    }
}
