package com.otus.alexshr.userinfo.di;

import com.otus.alexshr.userinfo.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivitiesBuilderModule {
    @ContributesAndroidInjector(modules = {FragmentsBuilderModule.class, MainActivityModule.class})

    @ActivityScope
    abstract MainActivity contributeMainActivity();
}
