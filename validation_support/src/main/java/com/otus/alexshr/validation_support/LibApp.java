package com.otus.alexshr.validation_support;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import timber.log.Timber;

import static com.otus.alexshr.validation_support.BuildConfig.DEBUG;

/**
 * Created by alexshr on 15.01.2019.
 */
public class LibApp extends Application {
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
    }
}
