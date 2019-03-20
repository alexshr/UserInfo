package com.otus.alexshr.userinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import lombok.Getter;
import lombok.Setter;
import timber.log.Timber;

public class ActivityViewModel extends ViewModel {

    @Getter
    @Setter
    private Navigator navigator;

    public ActivityViewModel(User user, Navigator navigator) {
        this.navigator = navigator;
        this.user = user;
        Timber.d("ActivityViewModel: %s", this);
    }

    @Getter
    @Setter
    private User user;

    public void init(AppCompatActivity activity) {
        navigator.setActivity(activity);
    }
}
