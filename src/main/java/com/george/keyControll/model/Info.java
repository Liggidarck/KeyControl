package com.george.keyControll.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Info {

    private int id;

    private String personName;

    private String personUid;

    private String cabinet;

    private String cabinetUid;

    private String dateTake;

    private String timeTake;

    private String timeReturn;

    public Info(String personName, String personUid,
                String cabinet, String cabinetUid,
                String dateTake, String timeTake,
                String timeReturn) {
        this.personName = personName;
        this.personUid = personUid;
        this.cabinet = cabinet;
        this.cabinetUid = cabinetUid;
        this.dateTake = dateTake;
        this.timeTake = timeTake;
        this.timeReturn = timeReturn;
    }
}
