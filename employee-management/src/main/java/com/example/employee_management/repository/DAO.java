package com.example.employee_management.repository;

import java.util.*;

public interface DAO<T> {

    List<T> getAll();
    Optional<T> getById(int id);
    boolean add(T t);
//    void update(T t);
    boolean delete(int id);
}
