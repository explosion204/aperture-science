package com.explosion204.aperture.sandbox2.data.jdbc.dao;

import com.explosion204.aperture.sandbox2.data.entity.User;

import java.util.Optional;

public interface UserDao {
  Optional<User> findById(long id);
  void create(User user);
}
