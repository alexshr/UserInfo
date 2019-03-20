package com.otus.alexshr.userinfo;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import timber.log.Timber;

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
        Timber.d("activity: %s", activity);
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new DisplayFragment())
                .addToBackStack(null)
                .commit();
    }

    public void setActivity(AppCompatActivity activity) {
        Timber.d("activity: %s", activity);
        this.activity = activity;
    }
}
