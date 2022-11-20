package com.george.keyControll.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Person {

    private int id;

    private String uid;

    private String name;

    private String image;

    private String cabinet;

    public Person(String uid, String name, String image, String cabinet) {
        this.uid = uid;
        this.name = name;
        this.image = image;
        this.cabinet = cabinet;
    }
}
