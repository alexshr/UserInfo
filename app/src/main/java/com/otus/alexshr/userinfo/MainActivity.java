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
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements Injectable, HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    Navigator navigator;

    @Inject
    ActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel.init(this);

        Timber.d(" ");

        navigator.setActivity(this);
        setContentView(R.layout.main_activity);

        if (savedInstanceState == null) {
            navigator.showInputFragment();
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
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    /*public void showInputFragment(){
        navigator.showInputFragment(this);
    }*/
}
