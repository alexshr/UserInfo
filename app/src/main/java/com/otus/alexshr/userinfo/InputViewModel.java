package com.otus.alexshr.userinfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import lombok.Getter;
import lombok.Setter;

public class InputViewModel extends ViewModel {
    @Getter
    @Setter
    private LiveData<Boolean> validationLiveData;
}
