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

    public void updateKey(Key key, int id) {
        try {
            keyRepository.updateKey(key, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteKey(int id) {
        try {
            keyRepository.deleteKey(id);
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

    public Key getKeyById(int id) {
        try {
            return keyRepository.getKeyById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Key getKeyByNumber(String keyNumber) {
        try {
            return keyRepository.getKeyByNumber(keyNumber);
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

}
