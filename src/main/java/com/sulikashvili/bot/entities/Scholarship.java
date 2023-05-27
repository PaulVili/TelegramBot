package com.sulikashvili.bot.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "scholarship_table")
public class Scholarship {
    @Id
    private Integer id;
    private String name;
    private String sum;

}
