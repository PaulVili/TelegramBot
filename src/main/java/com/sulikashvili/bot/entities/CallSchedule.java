package com.sulikashvili.bot.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "call_schedule_table")
public class CallSchedule {
    @Id
    private Integer id;
    private String name;
    private String startCalling;
    private String endCalling;
}
