package com.sulikashvili.bot.repositories;

import com.sulikashvili.bot.entities.Documents;
import org.springframework.data.repository.CrudRepository;

public interface DocumentsRepository extends CrudRepository<Documents, Integer>  {
}
