package com.george.keyControll.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Person {

    private int id;

    private String uid;

    private String name;


    public Person(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }
}
