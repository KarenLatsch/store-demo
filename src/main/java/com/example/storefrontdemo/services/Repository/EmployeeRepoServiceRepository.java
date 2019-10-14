package com.example.storefrontdemo.services.Repository;

import com.example.storefrontdemo.domain.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepoServiceRepository extends JpaRepository<Employee, Integer> {

    Employee findByUsername(String username);
}
