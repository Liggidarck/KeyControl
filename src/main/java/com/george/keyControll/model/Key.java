package com.george.keyControll.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Key {

    private int id;

    private String uid;

    private String personName;

    private String personImage;

    private String cabinet;

    private String dateTake;

    private String timeTake;

    private String timeReturn;

    public Key(String uid, String personName, String personImage, String cabinet,
               String dateTake, String timeTake, String timeReturn) {
        this.uid = uid;
        this.personName = personName;
        this.personImage = personImage;
        this.cabinet = cabinet;
        this.dateTake = dateTake;
        this.timeTake = timeTake;
        this.timeReturn = timeReturn;
    }
}
