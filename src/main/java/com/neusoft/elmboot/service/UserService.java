package com.neusoft.elmboot.service;

import com.neusoft.elmboot.po.User;

public interface UserService {
    User getUserByIdByPass(User user);
    User getUserById(String userId);
    int saveUser(User user);
    int updateUser(User user);
}
