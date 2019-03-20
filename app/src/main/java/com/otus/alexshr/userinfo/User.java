package com.otus.alexshr.userinfo;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import lombok.Data;

@Data
public class User {

    private MutableLiveData<String> name = new MutableLiveData<>(),
            email = new MutableLiveData<>(),
            phone = new MutableLiveData<>();
    private LiveData<Boolean> isValid = new MutableLiveData<>();

    @Inject
    public User() {
    }
}
