package org.example.service;

import org.example.classes.EmployeTache;

import java.util.List;

public interface EmployeTacheService {
    boolean create(EmployeTache et);
    boolean update(EmployeTache et);
    boolean delete(EmployeTache et);
    EmployeTache findById(int id);
    List<EmployeTache> findAll();
}
