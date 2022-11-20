package com.george.keyControll.viewModel;

import com.george.keyControll.model.Key;
import com.george.keyControll.repository.KeyRepository;

import java.sql.SQLException;
import java.util.ArrayList;

public class KeyViewModel {
    private final KeyRepository keyRepository;

    public KeyViewModel() {
        keyRepository = new KeyRepository();
    }

    public void createKey(Key key) {
        try {
            keyRepository.createKey(key);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Key getKeyByUid(String uid) {
        try {
            return keyRepository.getKeyByUid(uid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateKey(Key key) {
        try {
            keyRepository.updateKey(key);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Key> getAllKeys() {
        try {
            return keyRepository.getAllKeys();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Key> getKeyByDate(String dateTake) {
        try {
            return keyRepository.getKeysByDate(dateTake);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection()  {
        try {
            keyRepository.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
