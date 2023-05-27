package com.sulikashvili.bot.repositories;

import com.sulikashvili.bot.entities.CallSchedule;
import org.springframework.data.repository.CrudRepository;

public interface CallScheduleRepository extends CrudRepository<CallSchedule, Integer> {
}
