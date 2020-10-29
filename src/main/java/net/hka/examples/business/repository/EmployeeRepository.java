package net.hka.examples.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.hka.examples.business.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
