package com.example.employee_management.mapper;

import com.example.employee_management.entity.*;
import org.springframework.jdbc.core.RowMapper;
import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeRowMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        Employee employee = new Employee();
        employee.setId(rs.getInt("EmployeeID"));
        employee.setName(rs.getString("EmployeeName"));
        employee.setSalary(rs.getInt("Salary"));
        employee.setDateOfBirth(rs.getString("DateOfBirth"));

        Contact contact = new Contact();
        contact.setEmail(rs.getString("Email"));
        contact.setPhoneNumber(rs.getString("PhoneNumber"));

        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact);
        employee.setContacts(contacts);

        return employee;
    }

}