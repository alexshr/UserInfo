package com.otus.alexshr.userinfo.di;

import com.otus.alexshr.userinfo.DisplayFragment;
import com.otus.alexshr.userinfo.InputFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentsBuilderModule {

    @ContributesAndroidInjector(modules = InputFragmentModule.class)
    @InputFragmentScope
    abstract InputFragment contributeInputFragment();

    @ContributesAndroidInjector()
    abstract DisplayFragment contributeDisplayFragment();
}
