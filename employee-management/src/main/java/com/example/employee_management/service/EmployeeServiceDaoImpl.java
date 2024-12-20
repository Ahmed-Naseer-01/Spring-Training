package com.example.employee_management.service;

import com.example.employee_management.entity.Employee;
import com.example.employee_management.repository.DAO;
import com.example.employee_management.repository.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class EmployeeServiceDaoImpl implements ServiceDAO<Employee> {
    private EmployeeDao employeeDao;

    @Autowired
    public EmployeeServiceDaoImpl(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Transactional
    @Override
   public boolean add(Employee employee)
    {
        return employeeDao.add(employee);
    }

    @Override
    public boolean delete(int id){
        return employeeDao.delete(id);
    }

    @Override
    public Optional<Employee> getById(int id) {
        return employeeDao.getById(id);
    }
    @Override
    public List<Employee> getAll() {
        return employeeDao.getAll();
    }

    @Override
    public void update(int id, Employee employee) {
        employeeDao.update(id, employee);

    }
}
