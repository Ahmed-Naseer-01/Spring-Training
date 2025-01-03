package com.example.employee_management.repository;

import com.example.employee_management.entity.*;
import com.example.employee_management.mapper.EmployeeRowMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeDao implements DAO<Employee> {
    private static final Logger log = LoggerFactory.getLogger(EmployeeDao.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // add Employee
    @Transactional
    public boolean add(Employee employee) {
        boolean status = false;
        try {
            // Insert the Employee and retrieve generated id
            String sqlEmployee = "INSERT INTO Employee (name, salary, dateOfBirth) VALUES (?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            int employeeRowsAffected = jdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(sqlEmployee, Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, employee.getName());
                        ps.setInt(2, employee.getSalary());
                        ps.setString(3, employee.getDateOfBirth());
                        return ps;
                    },
                    keyHolder
            );

            // Retrieve the generated employee id
            if (employeeRowsAffected > 0) {
                int employeeId = keyHolder.getKey().intValue();
                employee.setId(employeeId); // Set the generated employee id

                // Insert Contacts for this employee using the generated employeeId
                for (Contact contact : employee.getContacts()) {
                    String sqlContact = "INSERT INTO Contact (phoneNumber, email, employee_id) VALUES (?, ?, ?)";
                    int contactRowsAffected = jdbcTemplate.update(sqlContact,
                            contact.getPhoneNumber(), contact.getEmail(), employeeId);

                    // If any contact insertion fails, rollback
                    if (contactRowsAffected <= 0) {
                        log.info("Failed to add contact to database");
                        return false; // Rollback happens here automatically due to @Transactional annotation
                    }
                }
                log.info("Successfully added contact to database");
                status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
        }
        log.info("Successfully added employee to database");
        return status;
    }

    // Delete Employee by ID
    public boolean delete(int id) {
        String sql = "DELETE FROM Employee WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }


    // Find Employee by ID
    public Optional<Employee> getById(int id) {
        String sql = "SELECT \n" +
                "    e.id AS EmployeeID,\n" +
                "  e.name AS EmployeeName, \n" +
                "  e.salary AS Salary, \n" +
                "  e.dateOfBirth AS DateOfBirth, \n" +
                "  c.email AS Email, " +
                "  c.phoneNumber AS PhoneNumber \n" +
                "FROM Employee e \n" +
                "LEFT JOIN Contact c \n" +
                "  ON e.id = c.employee_id \n" +
                "WHERE e.id = ? \n";

        try {
            Employee employee = jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), id);
            return Optional.ofNullable(employee);
        } catch (EmptyResultDataAccessException e) {
        //  if employee is null
            return Optional.empty();
        }
    }


    // Get All Employees
    public List<Employee> getAll() {
        String sql = "SELECT \n" +
                "    e.id AS EmployeeID,\n" +
                "    e.name AS EmployeeName,\n" +
                "    e.salary AS Salary,\n" +
                "    e.dateOfBirth AS DateOfBirth,\n" +
                "    c.email AS Email,\n" +
                "    c.phoneNumber AS PhoneNumber\n" +
                "FROM Employee e\n" +
                "LEFT JOIN Contact c \n" +
                "    ON e.id = c.employee_id;\n";
        return jdbcTemplate.query(sql, new EmployeeRowMapper());

    }

// Update Employee and also optionally update Contacts
    public void update(int id, Employee employee) {
        try {
            // Update Employee details
            String sqlUpdateEmployee = "UPDATE Employee SET name = ?, salary = ?, dateOfBirth = ? WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sqlUpdateEmployee,
                    employee.getName(),
                    employee.getSalary(),
                    employee.getDateOfBirth(),
                    id);

            if (rowsAffected > 0) {
                log.info("Employee with ID {} updated successfully", id);

                // If contacts are provided, update them
                if (employee.getContacts() != null && !employee.getContacts().isEmpty()) {
                    for (Contact contact : employee.getContacts()) {
                        // Check if the contact has an Id that is need to update
                        if (contact.getId() != 0) {
                            // Update existing Contact
                            String sqlUpdateContact = "UPDATE Contact SET phoneNumber = ?, email = ? WHERE employee_id = ?";
                            jdbcTemplate.update(sqlUpdateContact,
                                    contact.getPhoneNumber(),
                                    contact.getEmail(),
                                    contact.getId());
                            log.info("Updated contact with ID {}", contact.getId());
                        } else {
                            // Insert new Contact for this employee
                            String sqlInsertContact = "INSERT INTO Contact (phoneNumber, email, employee_id) VALUES (?, ?, ?)";
                            jdbcTemplate.update(sqlInsertContact,
                                    contact.getPhoneNumber(),
                                    contact.getEmail(),
                                    id);
                            log.info("Added new contact for employee with ID {}", id);
                        }
                    }
                }
            } else {
                log.warn("No Employee found with ID {}", id);
            }
        } catch (Exception e) {
            log.error("Error updating Employee with ID {}", id, e);
            throw new RuntimeException("Failed to update Employee and Contacts");
        }
    }
    }
