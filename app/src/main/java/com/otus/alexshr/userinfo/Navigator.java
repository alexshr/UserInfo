package com.otus.alexshr.userinfo;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by alexshr on 09.01.2019.
 */
public class Navigator {

    private AppCompatActivity activity;

    @Inject
    public Navigator() {
    }

    public void showInputFragment() {
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new InputFragment())
                .commit();
    }

    public void showDisplayFragment() {
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new DisplayFragment())
                .addToBackStack(null)
                .commit();
    }

    public void setActivity(AppCompatActivity activity) {
        //Timber.d("activity: %s", activity);
        this.activity = activity;
    }
}
