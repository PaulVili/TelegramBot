package com.sulikashvili.bot.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "teacher_office_table")
public class TeacherOffice {
    @Id
    private Integer id;
    private String firstName;
    private String secondName;
    private String thirdName;
    private String cabinet;
}
