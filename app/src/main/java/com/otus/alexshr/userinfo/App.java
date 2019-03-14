package com.otus.alexshr.userinfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;

import com.otus.alexshr.userinfo.di.AppInjector;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;

import static com.otus.alexshr.userinfo.BuildConfig.DEBUG;

/**
 * Created by alexshr on 09.01.2019.
 */
public class App extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> mDispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        if (DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @SuppressLint("DefaultLocale")
                @Override
                protected String createStackElementTag(@NonNull StackTraceElement element) {
                    return String.format("%s: %s:%d (%s)",
                            super.createStackElementTag(element),
                            element.getMethodName(),
                            element.getLineNumber(),
                            Thread.currentThread().getName());
                }
            });
        }

        AppInjector.init(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return mDispatchingAndroidInjector;
    }
}
