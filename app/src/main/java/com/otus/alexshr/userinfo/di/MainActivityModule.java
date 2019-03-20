package com.otus.alexshr.userinfo.di;

import com.otus.alexshr.userinfo.ActivityViewModel;
import com.otus.alexshr.userinfo.ActivityViewModelFactory;
import com.otus.alexshr.userinfo.MainActivity;
import com.otus.alexshr.userinfo.Navigator;
import com.otus.alexshr.userinfo.User;

import androidx.lifecycle.ViewModelProviders;
import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

@Module
class MainActivityModule {


    @Provides
    ActivityViewModel provideActivityViewModel(MainActivity activity, User user, Navigator navigator) {
        Timber.d("creating ActivityViewModel; activity: %s", activity);
        return ViewModelProviders.of(activity, new ActivityViewModelFactory(user, navigator)).get(ActivityViewModel.class);
    }
}
