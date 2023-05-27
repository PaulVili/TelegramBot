package com.sulikashvili.bot.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "addmission_hotline_phone_table")
public class AddmissionHotlinePhone {
    @Id
    private Integer id;
    private String hotlinePhone;
}
