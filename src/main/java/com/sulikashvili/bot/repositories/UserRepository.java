package com.sulikashvili.bot.repositories;

import com.sulikashvili.bot.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
