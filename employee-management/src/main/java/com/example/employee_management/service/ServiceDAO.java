package com.example.employee_management.service;

import java.util.*;

public interface ServiceDAO<T>{

    List<T> getAll();
    Optional<T> getById(int id);
    boolean add(T t);
    void update(int id, T t);
    boolean delete(int id);
}
