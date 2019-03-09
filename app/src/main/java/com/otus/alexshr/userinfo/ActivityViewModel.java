package com.otus.alexshr.userinfo;

import androidx.lifecycle.ViewModel;
import lombok.Getter;
import lombok.Setter;

public class ActivityViewModel extends ViewModel {
    @Getter
    @Setter
    private User user;
}
