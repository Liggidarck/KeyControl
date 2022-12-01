package com.george.keyControll.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Key {

    private int id;
    private String uid;
    private String number;
    private String available;

    public Key(String uid, String number, String available) {
        this.uid = uid;
        this.number = number;
        this.available = available;
    }
}
