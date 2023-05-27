package com.sulikashvili.bot.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "college_building_table")
public class Buildings {
    @Id
    private Integer id;
    private String building;
    private String address;
    private String phone;
    private String fax;
    private String email;
}
