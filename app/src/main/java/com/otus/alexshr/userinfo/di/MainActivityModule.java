package com.otus.alexshr.userinfo.di;

import com.otus.alexshr.userinfo.ActivityViewModel;
import com.otus.alexshr.userinfo.ActivityViewModelFactory;
import com.otus.alexshr.userinfo.MainActivity;

import androidx.lifecycle.ViewModelProviders;
import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

@Module
class MainActivityModule {


    @Provides
    ActivityViewModel provideActivityViewModel(MainActivity activity, ActivityViewModelFactory factory) {
        Timber.d("creating ActivityViewModel; activity: %s", activity);
        return ViewModelProviders.of(activity, factory).get(ActivityViewModel.class);
    }
}
