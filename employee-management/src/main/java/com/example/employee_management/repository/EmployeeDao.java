package com.example.employee_management.repository;

import com.example.employee_management.entity.*;
import com.example.employee_management.mapper.EmployeeRowMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.List;
@Repository
public class EmployeeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public boolean insertEmployee(Employee employee) {
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
                        return false; // Rollback happens here automatically due to @Transactional
                    }
                }
                status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
        }
        return status;
    }

    // Delete Employee by ID
    public boolean deleteEmployeeById(int id) {
        String sql = "DELETE FROM Employee WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    // Find Employee by ID
    public Employee findEmployeeById(int id) {
        String sql = "SELECT e.name AS EmployeeName, " +
                "e.salary AS Salary, " +
                "e.dateOfBirth AS DateOfBirth, " +
                "c.email AS Email, " +
                "c.phoneNumber AS PhoneNumber " +
                "FROM Employee e " +
                "LEFT JOIN Contact c ON e.id = c.employee_id " +
                "WHERE e.id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new EmployeeRowMapper());
    }

    // Get All Employees
    public List<Employee> getAllEmployees() {
        String sql = "SELECT \n" +
                "    e.name AS EmployeeName,\n" +
                "    e.salary AS Salary,\n" +
                "    e.dateOfBirth AS DateOfBirth,\n" +
                "    c.email AS Email,\n" +
                "    c.phoneNumber as PhoneNumber FROM Employee e" +
                "LEFT JOIN Contact c \n" +
                "    ON e.id = c.employee_id";
        return jdbcTemplate.query(sql, new EmployeeRowMapper());

    }
}
