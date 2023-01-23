package com.george.keyControll.viewModel;

import com.george.keyControll.model.Info;
import com.george.keyControll.repository.InfoRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InfoViewModel {
    private final InfoRepository infoRepository;

    public InfoViewModel() {
        infoRepository = new InfoRepository();
    }

    public void createInfo(Info info) {
        try {
            infoRepository.createInfo(info);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Info getInfoByPersonUid(String uid, String date) {
        try {
            return infoRepository.getInfoByPersonUidAndDate(uid, date);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Info getInfoByKeyUidAndDate(String uid, String date) {
        try {
            return infoRepository.getInfoByKeyUidAndDate(uid, date);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Info> getTransferList(String uid, String date) {
        try {
            return infoRepository.getListInfoByPersonUidAndDate(uid, date);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateInfo(Info info, int id) {
        try {
            infoRepository.updateInfo(info, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public ArrayList<Info> getInfoByDate(String today) {
        try {
            return infoRepository.getInfoByDate(today);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
