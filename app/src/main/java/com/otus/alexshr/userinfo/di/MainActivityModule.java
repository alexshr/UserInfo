package com.otus.alexshr.userinfo.di;

import com.otus.alexshr.userinfo.ActivityViewModel;
import com.otus.alexshr.userinfo.MainActivity;

import androidx.lifecycle.ViewModelProviders;
import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

@Module
class MainActivityModule {

    @Provides
    @ActivityScope
    ActivityViewModel provideActivityViewModel(MainActivity activity) {
        Timber.d("creating ActivityViewModel");
        return ViewModelProviders.of(activity).get(ActivityViewModel.class);
    }
}
