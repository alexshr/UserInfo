package com.otus.alexshr.userinfo;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import lombok.Getter;
import timber.log.Timber;

public class ActivityViewModel extends ViewModel {

    @Getter
    private Navigator navigator;

    @Getter
    private User user;

    @Inject
    public ActivityViewModel(User user, Navigator navigator) {
        this.navigator = navigator;
        this.user = user;
        Timber.d("ActivityViewModel: %s", this);
    }

    public void initNavigator(AppCompatActivity activity) {
        navigator.setActivity(activity);
    }

}
