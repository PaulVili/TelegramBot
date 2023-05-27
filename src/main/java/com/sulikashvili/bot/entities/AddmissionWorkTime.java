package com.sulikashvili.bot.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "addmission_work_time_table")
public class AddmissionWorkTime {
    @Id
    private Integer id;
    private String workTime;
}
