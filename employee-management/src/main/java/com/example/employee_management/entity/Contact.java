package com.example.employee_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Pattern(regexp = "^(?:\\+92|0092)?(03[0-5]{2})-?[0-9]{7}$|^(?:\\+92|0092)?([0-9]{3})-?[0-9]{7}$", message = "Only Pak Phone Number allowed")
    String phoneNumber;
    @Email(message = "Invalid email format")
    String email;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }
}