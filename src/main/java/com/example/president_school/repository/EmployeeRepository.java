package com.example.president_school.repository;

import com.example.president_school.entity.Employee;
import com.example.president_school.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByRole(Role role);

    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByPhone(String phone);

    List<Employee> findByRoleNot(Role role);
}
