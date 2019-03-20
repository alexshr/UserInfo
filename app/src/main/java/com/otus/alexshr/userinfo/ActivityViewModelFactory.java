package com.otus.alexshr.userinfo;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by alexshr on 20.03.2019.
 */

public class ActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private User user;
    private Navigator navigator;

    @Inject
    public ActivityViewModelFactory(User user, Navigator navigator) {
        this.user = user;
        this.navigator = navigator;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == ActivityViewModel.class) {
            return (T) new ActivityViewModel(user, navigator);
        }
        return null;
    }
}
