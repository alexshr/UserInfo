package com.otus.alexshr.userinfo;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import lombok.AllArgsConstructor;

/**
 * Created by alexshr on 20.03.2019.
 */
@AllArgsConstructor
public class ActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private User user;
    private Navigator navigator;

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == ActivityViewModel.class) {
            return (T) new ActivityViewModel(user, navigator);
        }
        return null;
    }
}
