package com.otus.alexshr.userinfo;

import android.os.Bundle;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import lombok.Data;

@Data
public class User {

    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";

    private MutableLiveData<String> name = new MutableLiveData<>(),
            email = new MutableLiveData<>(),
            phone = new MutableLiveData<>();

    @Inject
    public User() {
    }

    public void saveTo(Bundle bundle) {
        bundle.putString(KEY_NAME, name.getValue());
        bundle.putString(KEY_EMAIL, email.getValue());
        bundle.putString(KEY_PHONE, phone.getValue());
    }

    public void restoreFrom(Bundle bundle) {
        name.setValue(bundle.getString(KEY_NAME));
        email.setValue(bundle.getString(KEY_EMAIL));
        phone.setValue(bundle.getString(KEY_PHONE));
    }
}
