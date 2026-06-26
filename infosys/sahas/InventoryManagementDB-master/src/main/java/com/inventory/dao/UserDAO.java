package com.inventory.dao;

import com.inventory.model.User;
import java.util.List;

public interface UserDAO {
    boolean addUser(User user);
    User getUserById(int id);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean deleteUser(int id);
}