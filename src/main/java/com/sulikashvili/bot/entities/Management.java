package com.sulikashvili.bot.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "management_table")
public class Management {
    @Id
    private Integer id;
    private String name;
    private String post;
    private String phone;
    private String email;
}
