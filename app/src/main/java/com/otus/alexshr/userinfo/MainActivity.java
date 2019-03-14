package com.otus.alexshr.userinfo;

import android.os.Bundle;
import android.view.MenuItem;

import com.otus.alexshr.userinfo.di.Injectable;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements Navigator, Injectable, HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (savedInstanceState == null) {
            showInputFragment();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
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
                .commit();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
