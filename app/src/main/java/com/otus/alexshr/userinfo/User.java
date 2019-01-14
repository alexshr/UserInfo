package com.otus.alexshr.userinfo;

import lombok.Data;
import lombok.NonNull;

@Data
public class User {

    @NonNull
    private String name, email, phone;
}
