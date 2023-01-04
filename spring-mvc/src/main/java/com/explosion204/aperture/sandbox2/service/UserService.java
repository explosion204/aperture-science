package com.explosion204.aperture.sandbox2.service;

import com.explosion204.aperture.sandbox2.data.entity.User;
import com.explosion204.aperture.sandbox2.data.jdbc.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserDao userDao;

  public User findById(long id) {
    return userDao.findById(id)
            .orElseThrow();
  }

  public void create(User user) {
    userDao.create(user);
  }
}
