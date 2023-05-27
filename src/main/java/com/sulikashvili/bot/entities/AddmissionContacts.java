package com.sulikashvili.bot.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "addmission_contacts_table")
public class AddmissionContacts {
    @Id
    private Integer id;
    private String contacts;
}
