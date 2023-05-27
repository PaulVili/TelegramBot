package com.sulikashvili.bot.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "usersDataTable")
public class User {
    @Id
    private Long chat_id;
    private String firstName;
    private String lastName;
    private String userName;

    @Override
    public String toString() {
        return "User{" +
                "chat_id=" + chat_id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
