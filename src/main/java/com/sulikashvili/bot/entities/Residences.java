package com.sulikashvili.bot.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "student_residences_table")
public class Residences {
    @Id
    private Integer id;
    private String number;
    private String address;
    private String phone;

}
